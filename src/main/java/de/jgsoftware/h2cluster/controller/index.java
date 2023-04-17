package de.jgsoftware.h2cluster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import de.jgsoftware.h2cluster.controller.interfaces.ih2index;

/**
 *
 * @author hoscho
 */
@Controller
public class index implements ih2index
{

    ModelAndView mv;

    @Override
    public ModelAndView index()
    {
        mv = new ModelAndView("index");

        return mv;
    }
}
