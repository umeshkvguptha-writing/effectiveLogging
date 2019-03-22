package com.ssa.effective.logging.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class ServletFilter implements Filter{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		StopWatch sw = new StopWatch();
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			
			// Set UNIQUE ID in MDC (Thread Local)
			// MDC Info: https://www.slf4j.org/api/org/slf4j/MDC.html
			// MDC Object will be used in logging.pattern.level
			MDC.put("UNIQUE-ID", httpRequest.getHeader("UNIQUE-ID"));
			
			sw.start();
			LOGGER.info("Start Request: {}", httpRequest.getRequestURI());
			chain.doFilter(request, response);
			
		} finally {
			sw.stop();
			LOGGER.info("End of Request: took {} ms", sw.getLastTaskTimeMillis());
			
			// Clear MDC (Thread Local)
			MDC.clear();
		}
		
	}

}
