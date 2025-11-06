package com.pet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 모든 요청/응답에 문자 인코딩(기본 UTF-8)을 적용하는 필터입니다.
 * - web.xml 또는 애노테이션으로 매핑하여 사용합니다.
 * - form 데이터가 깨지지 않도록 request/response에 인코딩을 지정합니다.
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {
    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
        // web.xml의 <init-param>으로 원하는 인코딩을 지정할 수 있습니다.
        String enc = filterConfig.getInitParameter("encoding");
        if (enc != null) encoding = enc;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 요청에 아직 인코딩이 설정되지 않았다면 지정한 인코딩을 적용
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        // 응답 인코딩도 통일
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }
}
