package com.ggbook.constants;

/**
 * 常量
 * @author lanyongmao
 *
 */
public class Constant {

	/**默认分页大小*/
	public static final int PAGE_SIZE = 10;

	/**存储技术*/
	public static class Storage {
		/**后台登录人员session*/
		public static final String ROOT_SESSION = "root_session";
		public static final String DASHBOARD = "DASHBOARD";
		/**websocket*/
		public static final String FRIEND_WEBSOCKET = "FRIEND_WEBSOCKET";
		/**当前在线朋友*/
		public static final String CURR_FRIEND = "CURR_FRIEND";
		/**密码狗密钥*/
		public static final String RYKEY = "RYKEY";
		/**客户端登录IDKEY*/
		public static final String CLIENT_USER_ID = "CLIENT_USER_ID";
		/**客户端本机IP*/
		public static final String LOCAL_IP = "LOCAL_IP";
	}

	/**系统类型*/
	public static class Sys {
		public static final String SERVICE = "service";
		public static final String CLIENT = "client";
	}

	/**系统类型*/
	public static class SysLevel {
		public static String[] LEVELARR = { "零级", "一级", "二级", "三级", "四级", "五级", "六级", "七级", "八级", "九级", "十级" };
	}

	/**后台用户常量*/
	public static class CUser {
		/**类型*/
		public static class Type{
			/**服务端*/
			public static final int SERVICE = 1;
			/**客户端*/
			public static final int CLIENT = 2;
		}
		/**状态*/
		public static class Status{
			/**正常*/
			public static final int ABLE = 2;
			/**禁用*/
			public static final int UNABLE = 1;
		}
	}

	/**系统参数常量*/
	public static class CDashboard {
		/**状态*/
		public static class Status{
			/**启用*/
			public static final int ABLE = 2;
			/**禁用*/
			public static final int UNABLE = 1;
		}
		/**参数键*/
		public static class Key{
			/**商家列表*/
			public static final String MCHS = "MCHS";
			/**订单超时分钟数*/
			public static final String TIMEOUT_MINUTE = "TIMEOUT_MINUTE";
		}
	}

	/**公告帮助*/
	public static class CNotice{
		/**状态*/
		public static class Status{
			/**正常*/
			public static final int ABLE = 2;
			/**禁用*/
			public static final int UNABLE = 1;
		}
		/**类型*/
		public static class Type{
			/**公告*/
			public static final int NOTICE = 1;
			/**帮助*/
			public static final int QA = 2;
		}
	}

	/**分类*/
	public static class CCategory{
		/**等级*/
		public static class TOP{
			/**指导书*/
			public static final int GUIDE = 1;
			/**工具类*/
			public static final int TOOL = 2;
			/**等保*/
			public static final int GRADE = 3;
		}
	}

	/**知识库*/
	public static class CRepertory{
		/**类型*/
		public static class Type{
			/**安全层面*/
			public static final int LEVEL = 1;
			/**控制点*/
			public static final int POINT = 2;
			/**控制项*/
			public static final int ITEM = 3;
		}
		/**顶层父ID*/
		public static class DEFAULT{
			/**默认父ID*/
			public static final int ZERO = 0;
			/**默认父ID*/
			public static final int POS = 1000;
		}
	}

	/**客户单位*/
	public static class CArchives{
		/**状态*/
		public static class Status{
			/**正常*/
			public static final int ABLE = 1;
			/**禁用*/
			public static final int UNABLE = 0;
		}
	}

	/**知识库*/
	public static class CStandard{
		/**类型*/
		public static class Type{
			/**普通*/
			public static final int ORDINARY = 1;
			/**特殊*/
			public static final int SPECIAL = 2;
		}
	}

	/**项目*/
	public static class CProject{
		/**状态*/
		public static class Status{
			/**正常*/
			public static final int ABLE = 2;
			/**禁用*/
			public static final int UNABLE = 1;
		}
		/**版本*/
		public static class Version{
			/**当前版本*/
			public static final String cur = "cur";
		}
		/**版本*/
		public static class Step{
			public static final int GRADING = 1;//定级
			public static final int INQUIRY = 2;//调查
			public static final int DISPARITY = 3;//差距测评
			public static final int CHANGED = 4;//整改阶段
			public static final int ACCEPTANCE = 5;//验收测评
			public static final int RECEIPT = 6;//回执
		}
	}

	/**项目扩展*/
	public static class CProjectExt{
		/**类型*/
		//1：项目概述 4：整体测评 5：风险分析和评价 6：总体评价
		public static class Type{
			/**项目启动*/
			public static final int START = 0;
			/**系统概述*/
			public static final int DESC = 1;
			/**系统构成*/
			public static final int CONS = 2;
			/**现场核查*/
			public static final int CHECK = 3;
			/**层面测评*/
			public static final int LEVEL_ASSESS = 7;
			/**用户与设备的关系*/
			public static final int JOB = 8;
		}

		/**版本*/
		public static class ExtVersion{
			/**当前版本*/
			public static final String CUR = "cur";
		}
	}

	/**
	 * 加密狗错误代码
	 */
	public static class RY3 {
		public static final String SUCCESS = "0x00000000"; //操作成功
		public static final String NOT_FOUND = "0xF0000001"; //未找到指定的设备
		public static final String INVALID_PARAMETER = "0xF0000002";//参数错误
		public static final String COMM_ERROR = "0xF0000003";//通讯错误
		public static final String INSUFFICIENT_BUFFER = "0xF0000004";//缓冲区空间不足
		public static final String NO_LIST = "0xF0000005";//没有找到设备列表
		public static final String DEVPIN_NOT_CHECK = "0xF0000006";//开发商户口令没有验证
		public static final String USERPIN_NOT_CHECK = "0xF0000007";//用户口令没有验证
		public static final String RSA_FILE_FORMAT_ERROR = "0xF0000008";//RSA文件格式错误
		public static final String RSA_DIR_NOT_FOUND = "0xF0000009";//目录没有找到
		public static final String ACCESS_DENIED = "0xF000000A";//访问被拒绝
		public static final String ALREADY_INITIALIZED = "0xF000000B";//产品已经初始化
		public static final String INCORRECT_PIN = "0xF0000C00";//密码不正确
		public static final String DF_SIZE = "0xF000000D";//指定的目录空间大小不够
		public static final String FILE_EXIST = "0xF000000E";//文件已存在
		public static final String UNSUPPORTED = "0xF000000F";//功能不支持或尚未建立文件系统
		public static final String FILE_NOT_FOUND = "0xF0000010";//未找到指定的文件
		public static final String ALREADY_OPENED = "0xF0000011";//卡已经被打开
		public static final String DIRECTORY_EXIST = "0xF0000012";//目录已存在
		public static final String CODE_RANGE = "0xF0000013";//虚拟机内存地址溢出
		public static final String INVALID_POINTER = "0xF0000014";//虚拟机错误的指针
		public static final String GENERAL_FILESYSTEM = "0xF0000015";//常规文件系统错误
		public static final String OFFSET_BEYOND = "0xF0000016";//文件偏移量超出文件大小
		public static final String FILE_TYPE_MISMATCH = "0xF0000017";//文件类型不匹配
		public static final String PIN_BLOCKED = "0xF0000018";//PIN码琐死
		public static final String INVALID_HANDLE = "0xF0000019";//无效的句柄
		public static final String ERROR_UNKNOWN = "0xFFFFFFFF";//未知错误
		public static final String FUNC_EXPIRED = "0x00000017";//COS已过期
		public static final String HW_CLOCK_BROKEN = "0x00000018";//硬件时钟损坏
		public static final String RYKEY = "RYKEY";//密码狗密钥
		public static final String RYTIME = "RYTIME";//密码狗上次检查时间
		public static final String RY = "RY";//密码狗对象
		public static final String RY_KEY = "@GOGO#chinaGDN!%";
	}

	/**动作权限*/
	public static class CAction{
		/**类型*/
		public static class Type{
			/**菜单*/
			public static final int ACTION = 1;
			/**action*/
			public static final int MENU = 2;
			/**shows*/
			public static final int SHOWS = 3;
		}
	}

	/**测评数据存储类型*/
	public static class CMODE{

		public static final String REDIS = "redis";

		public static final String MONGO = "mongo";
	}
}
