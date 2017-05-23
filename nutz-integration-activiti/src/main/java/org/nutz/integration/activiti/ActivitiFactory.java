package org.nutz.integration.activiti;

import javax.sql.DataSource;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.scripting.ResolverFactory;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Mirror;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class ActivitiFactory {
    
    private static final Log log = Logs.get();
    
    public static ProcessEngineConfiguration build(DataSource ds, PropertiesProxy conf, ResolverFactory rf) {
        StandaloneProcessEngineConfiguration spec = new StandaloneProcessEngineConfiguration();
        spec.setDataSource(ds);
        Mirror<StandaloneProcessEngineConfiguration> mirror = Mirror.me(StandaloneProcessEngineConfiguration.class);
        for (String key : conf.keys()) {
            if (!key.startsWith("activiti."))
                continue;
            if (log.isDebugEnabled())
                log.debugf("%s=%s", key, conf.get(key));
            mirror.setValue(spec, key.substring("activiti.".length()), conf.get(key));
        }
        if (log.isDebugEnabled())
            log.debug("databaseSchemaUpdate = [" + spec.getDatabaseSchemaUpdate() + "]");
        if (rf != null)
            spec.getResolverFactories().add(rf);
        return spec;
    }
}
