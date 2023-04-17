package de.jgsoftware.h2cluster.controller.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author hoscho
 */

@RequestMapping("/")
public interface ih2index
{


    @GetMapping({"index", "/"})
    ModelAndView index();
}
