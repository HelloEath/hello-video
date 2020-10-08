DROP TABLE IF EXISTS t_video;

CREATE TABLE t_video
(
	id BIGINT(20) NOT NULL COMMENT '主键ID',
	vname  Text DEFAULT NULL COMMENT '名字',
	orina_Name Text NULL DEFAULT NULL COMMENT '别名',
	main_Class VARCHAR(1) NULL DEFAULT NULL COMMENT '分类',
	calss_Name Text NULL DEFAULT NULL COMMENT '类名',
	type Text NULL DEFAULT NULL COMMENT '类型',
	region VARCHAR(30) NULL DEFAULT NULL COMMENT '地区',
	vyear VARCHAR(4) NULL DEFAULT NULL COMMENT '年份',
	fist_Name VARCHAR(1) NULL DEFAULT NULL COMMENT '字母',
	score DOUBLE NULL DEFAULT 0.0 COMMENT '评分',
	img_Url  Text NULL DEFAULT null COMMENT '剧照',
	state  VARCHAR(30) NULL DEFAULT null COMMENT '状态',
	all_Episodes  VARCHAR(30) NULL DEFAULT null COMMENT '集数',
	last_Episodes  VARCHAR(30) NULL DEFAULT null COMMENT '最新集数',
	season  int(1) NULL DEFAULT null COMMENT '季',
	actor  Text NULL DEFAULT null COMMENT '主演',
	lang  VARCHAR(30) NULL DEFAULT null COMMENT '语言',
	time  VARCHAR(30) NULL DEFAULT null COMMENT '时长',
	vdesc  VARCHAR(30) NULL DEFAULT null COMMENT '描述',
	resolution  VARCHAR(30) NULL DEFAULT null COMMENT '分辨率',
		del  int(1) NULL DEFAULT null COMMENT '是否删除',
		create_Time DATE NULL DEFAULT null  COMMENT '创建时间',
		update_Time DATE NULL DEFAULT null  COMMENT '更新时间',

	PRIMARY KEY (id)
);

DROP TABLE IF EXISTS t_video_down_load;

CREATE TABLE t_video_down_load
(
	id BIGINT(20) NOT NULL COMMENT '主键ID',
	video_Id BIGINT(20) NOT NULL COMMENT '外键ID',
	vname Text NULL DEFAULT NULL COMMENT '名字',
	online_Type Text NULL DEFAULT NULL COMMENT '地址类型',
	download_Url Text NULL DEFAULT NULL COMMENT '下载地址',
		del  int(1) NULL DEFAULT null COMMENT '是否删除',
		create_Time DATE NULL DEFAULT null  COMMENT '创建时间',
		update_Time DATE NULL DEFAULT null  COMMENT '更新时间',

	PRIMARY KEY (id)
);


DROP TABLE IF EXISTS t_video_online;

CREATE TABLE t_video_online
(
		id BIGINT(20) NOT NULL COMMENT '主键ID',
	video_Id BIGINT(20) NOT NULL COMMENT '外键ID',
		vname Text NULL DEFAULT NULL COMMENT '名字',
	online_Type VARCHAR(50) NULL DEFAULT NULL COMMENT '地址类型',
	online_Watch_Url Text NULL DEFAULT NULL COMMENT '在线看地址',
		linke_Url Text NULL DEFAULT NULL COMMENT '连接地址',
		del  int(1) NULL DEFAULT null COMMENT '是否删除',
		create_Time DATE NULL DEFAULT null  COMMENT '创建时间',
		update_Time DATE NULL DEFAULT null  COMMENT '更新时间',

	PRIMARY KEY (id)
);