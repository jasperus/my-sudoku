package web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SudokuController {

	private static Logger logger = LogManager.getLogger();

//	@RequestMapping(value = "/demo", method = { RequestMethod.POST })
//	public ModelAndView reloadAllReports(HttpServletRequest request) {
//		try {
//			return Generator
//			return listAllReports(request.getSession(),
//					messageSource.getMessage("reports.reload", null, RequestContextUtils.getLocale(request)),
//					LEVEL_SUCCESS);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return listAllReports(request.getSession(), message, LEVEL_ERROR);
//		}
//	}
}
