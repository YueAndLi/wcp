package com.farm.web.init;

import com.farm.parameter.FarmParameterService;
import com.farm.wcp.api.WcpAppInter;
import com.farm.web.rmi.impl.WcpAppImpl;
import com.farm.web.task.ServletInitJobInter;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class InitRmiInter implements ServletInitJobInter {
    private static final Logger log = Logger.getLogger(InitRmiInter.class);

    @Override
    public void execute(ServletContext context) {
        try {
            if (FarmParameterService.getInstance().getParameter("wcp.rmi.state").toUpperCase().equals("FALSE")) {
                return;
            }
            int port = Integer.valueOf(FarmParameterService.getInstance().getParameter("wcp.rmi.port"));
            String localRmiIp = FarmParameterService.getInstance().getParameter("wcp.rmi.ip");
            String rui = "rmi://" + localRmiIp + ":" + port + "/wcpapp";
            WcpAppInter wda = new WcpAppImpl();
            LocateRegistry.createRegistry(port);
            Naming.rebind(rui, wda);
            log.info("启动RMI服务" + rui);
        } catch (RemoteException e) {
            System.out.println("创建远程对象发生异常！");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println("发生重复绑定对象异常！");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("发生URL畸形异常！");
            e.printStackTrace();
        }

    }

}
