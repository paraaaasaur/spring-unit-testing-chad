package com.herbivore.springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//@Configuration
public class SemanticConfig {

	/**
	 * 🌍 The Front Controller.
	 * All requests pass through this servlet.
	 * It delegates to HandlerMappings, Adapters, and Resolvers.
	 */
	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

	/**
	 * 🗺️ Maps incoming requests to specific handler methods.
	 * Spring Boot auto-registers several mappings; this one
	 * handles @RequestMapping, @GetMapping, etc.
	 */
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
		// Optionally, customize order or add interceptors here
		System.out.println("✅ RequestMappingHandlerMapping initialized");
		return mapping;
	}

	/**
	 * 🧩 Knows *how* to invoke a handler once it's found.
	 * Resolves method arguments (@RequestParam, @PathVariable, etc.)
	 * and handles return values (views, @ResponseBody, etc.).
	 */
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();

		// Show what argument resolvers are available
		System.out.println("✅ HandlerMethodArgumentResolvers registered:");
		adapter.getArgumentResolvers().forEach(r ->
				System.out.println("   - " + r.getClass().getSimpleName()));

		// Show what return value handlers are available
		System.out.println("✅ HandlerMethodReturnValueHandlers registered:");
		adapter.getReturnValueHandlers().forEach(r ->
				System.out.println("   - " + r.getClass().getSimpleName()));

		return adapter;
	}

	/**
	 * 🧱 Converts Java objects to/from HTTP payloads (JSON, XML, etc.).
	 * Usually configured automatically by Boot via Jackson.
	 */
	@Bean
	public List<HttpMessageConverter<?>> messageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new MappingJackson2HttpMessageConverter());
		System.out.println("✅ MessageConverters: JSON enabled");
		return converters;
	}

	/**
	 * 🧭 Resolves view names (like "home") to actual templates (home.html, JSP, etc.).
	 * Only used for non-@ResponseBody controllers that return a view name.
	 */
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		System.out.println("✅ ViewResolver registered");
		return resolver;
	}

	/**
	 * 💥 Handles exceptions thrown during request processing.
	 * This example uses a very simple resolver.
	 */
	@Bean
	public SimpleMappingExceptionResolver exceptionResolver() {
		SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
		Properties mappings = new Properties();
		mappings.setProperty("Exception", "error"); // map Exception -> error.jsp
		resolver.setExceptionMappings(mappings);
		System.out.println("✅ ExceptionResolver registered");
		return resolver;
	}
}