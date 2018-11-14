package com.farm.wda.tag;

import com.farm.core.ParameterService;
import com.farm.parameter.FarmParameterService;
import com.farm.wda.inter.WdaAppInter;

import java.rmi.Naming;

public class RmiServer {
    private static WdaAppInter personService = null;

    public static WdaAppInter getInstance() {
        try {
            if (personService == null) {
                ParameterService parameterService = FarmParameterService.getInstance();
                String wdaRmiUrl = parameterService.getParameter("config.wda.rmi.url");
                String wdaRmiIp = parameterService.getParameter("wda.rmi.ip");
                String wdaRmiPort = parameterService.getParameter("wda.rmi.port");
                wdaRmiUrl = wdaRmiUrl.replaceAll("WDA_RMI_IP", wdaRmiIp).replaceAll("WDA_RMI_PORT", wdaRmiPort);
                personService = (WdaAppInter) Naming.lookup(wdaRmiUrl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return personService;
    }
}
