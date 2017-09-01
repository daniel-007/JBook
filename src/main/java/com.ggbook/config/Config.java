package com.ggbook.config;

import com.jfinal.config.*;
import com.jfinal.core.Const;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.ParamNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.ggbook.model.BaseModel;
import net.dreamlu.event.EventPlugin;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

	/**D
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		//设置上传路径
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")){
			me.setBaseUploadPath(PropKit.get("upload.window.path"));
		}else{
			me.setBaseUploadPath(PropKit.get("upload.linux.path"));
		}
		me.setMaxPostSize(100 * Const.DEFAULT_MAX_POST_SIZE);
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		try {
			me.add(new AutoBindRoutes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 配置插件
	 */
	@SuppressWarnings("unchecked")
	public void configPlugin(Plugins me) {

		try {
			//自动加载model
			//配置C3p0数据库连接池插件
			C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
			c3p0Plugin.setMaxPoolSize(800);
			c3p0Plugin.setMaxIdleTime(60);
			me.add(c3p0Plugin);
			AutoTableBindPlugin atbp = new AutoTableBindPlugin(c3p0Plugin, ParamNameStyles.lowerUnderlineModule("t"));
			atbp.addExcludeClasses(BaseModel.class);
			atbp.setShowSql(false);
			me.add(atbp);

			/*me.add(new EhCachePlugin());//缓存插件
			me.add(new QuartzPlugin("job.properties"));//任务调度插件


			try {
				//redis插件
				RedisPlugin newsRedis = new RedisPlugin("REDIS", PropKit.get("redis.uri"), PropKit.getInt("redis.port"), 10000);
				me.add(newsRedis);
			} catch (Exception e) {
			}

			try {
				//mongodb插件
				MongodbPlugin mongodbPlugin = new MongodbPlugin(PropKit.get("mongodbUrl"), PropKit.getInt("mongodbPort"), PropKit.get("mongodb"));
				me.add(mongodbPlugin);
			} catch (Exception e) {
			}*/

			//初事件通知插件
			EventPlugin eventplugin = new EventPlugin();
			eventplugin.async();// 开启全局异步
			eventplugin.scanJar();// 设置扫描jar包，默认不扫描
			eventplugin.scanPackage("com.ggbook");// 设置监听器默认包，默认全扫描
			me.add(eventplugin);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new UrlSkipHandler(".*/nyt.*",false));
	}
	/**
	 * 地区基础数据
	 */
	public void afterJFinalStart() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 805, "/", 5);
	}
}
