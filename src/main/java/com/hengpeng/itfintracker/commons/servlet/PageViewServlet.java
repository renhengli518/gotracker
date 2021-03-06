package com.hengpeng.itfintracker.commons.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hengpeng.itfintracker.commons.utils.AddressAttr;
import com.hengpeng.itfintracker.commons.utils.AddressUtils;
import com.hengpeng.itfintracker.commons.utils.PageViewUtil;
import com.hengpeng.itfintracker.entity.UserBehaviorRecord;
import com.hengpeng.itfintracker.service.PageViewService;

/**
 * 网站用户行为数据收集action类
 * 
 * @author renhengli
 */
public class PageViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1405586932831167454L;

	private PageViewService pageViewService;

	/**
	 * 获取前端JS参数，并传入数据库
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
		/**
		 * 获取封装参数
		 */
		Map<String, String> parameterMap = new HashMap<String, String>();
		Enumeration<String> params = request.getParameterNames();
		try {
			while (params.hasMoreElements()) {
				String paramName = params.nextElement();
				parameterMap.put(paramName, URLDecoder.decode(request.getParameter(paramName), "utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String userurgent = parameterMap.get("s_iev");// 浏览器版本
		String sessionId = request.getSession().getId();
		String ip = PageViewUtil.getIpAddr(request);// IP地址
		ip = PageViewUtil.getStringInLength(ip, 32);
		String country = "";
		String province = "";
		String city = "";
		String address = AddressUtils.getAddresses("ip=" + ip, "UTF-8");
		if (StringUtils.isNotEmpty(address)) {
			ObjectMapper op = new ObjectMapper();
			AddressAttr addressAttr = op.readValue(address, AddressAttr.class);
			Object data = addressAttr.getData();
			Map<String, String> adds = (Map<String, String>) data;
			country = adds.get("country");
			province = adds.get("region");
			city = adds.get("city");
		}

		String clientSystem = parameterMap.get("s_plt");// 客户端系统
		String clientResolution = parameterMap.get("s_rst");// 客户分辨率
		String clientPageType = parameterMap.get("s_ct");// 客户端页面类型
		String bd_str = parameterMap.get("bd");// 行为记录参数
		String refferPage = parameterMap.get("refferPage");
		Map<String, String> bd_map = new HashMap<String, String>();
		if (bd_str != null && !"".equals(bd_str)) {
			bd_str = bd_str.substring(1, bd_str.length() - 1);
			String str_array[] = bd_str.split("\\|");
			for (String temp_str : str_array) {
				bd_map.put(temp_str.split("\\=")[0], temp_str.split("=")[1]);
			}
		}
		String buttonPosition = bd_map.get("buttonPosition");
		String endUserId = bd_map.get("endUserId");
		Date clientTime = new Date(Long.parseLong(bd_map.get("b_clt")));
		String stringDate = DateFormatUtils.format(clientTime, "yyyy-MM-dd");
		String pageUrl = bd_map.get("pageUrl");
		// String country = bd_map.get("country");
		// String province = bd_map.get("province");
		// String city = bd_map.get("city");
		String stayTime = bd_map.get("stayTime");
		Long stayTimeMilSeconds = Long.valueOf(bd_map.get("stayTimeMilSeconds"));
		String pageTitle = bd_map.get("pageTitle");
		//String refferPage = bd_map.get("refferPage");
		String fromWhere = bd_map.get("fromWhere");
		String serachKeyWords = bd_map.get("serachKeyWords");
		String linkPosition = bd_map.get("linkPosition");
		String viewType = bd_map.get("viewType");
		String browserType = bd_map.get("browserType");
		String browserVersion = bd_map.get("browserVersion");
		Boolean isMobile = Boolean.valueOf(bd_map.get("is_mobile"));
		String completeDeviceName = bd_map.get("complete_device_name");
		String formFactor = bd_map.get("form_factor");
		List<UserBehaviorRecord> list = this.pageViewService.getUserBehaviorByIp(ip);
		String newUserFlag = "YES";
		if (CollectionUtils.isNotEmpty(list) && list.size() > 0) {
			newUserFlag = "NO";
		}
		/**
		 * 添加记录，写入MySQL
		 */
		UserBehaviorRecord behaviorUser = new UserBehaviorRecord(buttonPosition, linkPosition, viewType, ip, sessionId, endUserId, clientTime,
				newUserFlag, userurgent, pageUrl, country, province, city, stayTime, stayTimeMilSeconds, pageTitle, refferPage, clientSystem,
				clientResolution, clientPageType, fromWhere, serachKeyWords, stringDate, browserType, browserVersion, isMobile, completeDeviceName,
				formFactor);
		this.pageViewService.savePageViewInfoToDB(behaviorUser);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
		doGet(request, response);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		pageViewService = (PageViewService) ctx.getBean("pageViewService");
	}
}