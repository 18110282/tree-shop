package com.treeshop.service;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.servlet.http.HttpServletRequest;

@Service
public class ThymeleafService {
    private static final String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessages";
    private static final String MAIL_TEMPLATE_PREFIX = "/templates/views/client/";
    private static final String MAIL_TEMPLATE_SUFFIX = ".html";
    private static final String UTF_8 = "UTF-8";

    //    private static final String TEMPLATE_NAME = "mail-template";
    private static final String TEMPLATE_NAME = "invoice1";

    private static final TemplateEngine templateEngine;

    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

    private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    static {
        templateEngine = emailTemplateEngine();
    }
    public String getContent(HttpServletRequest request) {
        final Context context = new Context();

        context.setVariable("siteURL", request.getRequestURL().toString().replace(request.getServletPath(), ""));
//        context.setVariable("project_name", "spring-email-with-thymeleaf Demo");

        return templateEngine.process(TEMPLATE_NAME, context);
    }
}
