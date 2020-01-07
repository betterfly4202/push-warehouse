CREATE TABLE `tb_autopush_interface_inform` (
  `push_seq` int(11) NOT NULL AUTO_INCREMENT,
  `curdate` varchar(30) NOT NULL COMMENT '발송일',
  `send_plan_time` varchar(30) NOT NULL DEFAULT '12:00:00' COMMENT '발송목표 시간',
  `sort_no` int(10) unsigned NOT NULL,
  `mobile_token` varchar(255) NOT NULL,
  `device` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1: ios 2: andriod',
  `m_id` int(10) unsigned NOT NULL DEFAULT '0',
  `cmpn_no` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT 'autopush cmpn_no',
  `cmpn_seq` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '하루에 같은 캠페인이 여러번 등록될 경우+1',
  `test_group` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT 'ab테스트용. 1이 기존',
  `title` varchar(30) NOT NULL DEFAULT '' COMMENT '푸시제목',
  `content` varchar(60) NOT NULL DEFAULT '' COMMENT '푸시내용',
  `image` varchar(250) NOT NULL DEFAULT '' COMMENT '이미지URL',
  `event_landing_url` varchar(400) NOT NULL,
  `deal_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '푸시 발송시 배송2.0 상품번호/딜번호, 레거시 deal_id가 있는 경우만 사용. 나머지는 0',
  `utm_source` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '트래킹용 코드(cmpn_no)',
  `utm_medium` varchar(30) NOT NULL DEFAULT 'app_push' COMMENT '트래킹용 코드(app_push)',
  `utm_campaign` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '트래킹용 코드(날짜,cmpn_seq)',
  `utm_content` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '트래킹용 코드(deal_id)',
  KEY `idx_send_plan_time` (`send_plan_time`),
  KEY `curdate` (`curdate`,`mobile_token`,`cmpn_no`,`cmpn_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='앱푸시 발송용'
;

CREATE TABLE `tb_campaign` (
  `campaign_seq` int(11) NOT NULL AUTO_INCREMENT,
  `campaign_name` varchar(64) NOT NULL,
  `service_seq` int(11) NOT NULL,
  `sys_user_seq` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL COMMENT '캠페인 활성화 여부 (true/false)',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '0 : 생성 중\n1 : 생성 완료\n2 : 발송 중\n3 : 발송 완료\n4 : 발송 중지 (is_active가 false가 될 때도 발송중지 상태로 둠)\n',
  `start_time` datetime DEFAULT NULL COMMENT '캠페인 시작 시간. 이 시간 이전은 스케줄에 등록되지 않음 ',
  `end_time` datetime DEFAULT NULL COMMENT '캠페인 종료시간. 스케줄에 등록될 때는 이시간 이후는 스케줄에서 제외됨 ',
  `comment` varchar(1024) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`campaign_seq`),
  KEY `fk_tb_campaign_tb_sys_users1_idx` (`sys_user_seq`),
  KEY `fk_tb_campaign_tb_services1_idx` (`service_seq`),
  CONSTRAINT `fk_tb_campaign_tb_services1` FOREIGN KEY (`service_seq`) REFERENCES `tb_services` (`service_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_campaign_tb_sys_users1` FOREIGN KEY (`sys_user_seq`) REFERENCES `tb_sys_users` (`sys_user_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8


CREATE TABLE `tb_campaign_history` (
  `campaign_seq` int(11) NOT NULL,
  `campaign_item_seq` int(11) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `comment` varchar(256) DEFAULT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE `tb_campaign_items` (
  `campaign_item_seq` int(11) NOT NULL AUTO_INCREMENT,
  `campaign_seq` int(11) NOT NULL,
  `message_seq` int(11) DEFAULT NULL,
  `comment` varchar(1024) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '삭제 유무. \nTrue : 삭제됨\nFalse : 삭제 안됨',
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`campaign_item_seq`),
  KEY `fk_tb_campaign_items_tb_campaign1_idx` (`campaign_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=2064 DEFAULT CHARSET=utf8

CREATE TABLE `tb_campaign_stat` (
  `campaign_stat_seq` int(11) NOT NULL AUTO_INCREMENT COMMENT '발송 성공/실패/cr 등에 대한 통계 정보',
  `campaign_seq` int(11) NOT NULL,
  `campaign_item_seq` int(11) NOT NULL,
  `status` int(11) NOT NULL COMMENT '발송 진행 상태\n0 : 발송 전\n1 : 발송 중\n9 : 발송 완료',
  `send_total` int(11) NOT NULL DEFAULT '0' COMMENT '전체 발송 대상 건수',
  `send_success` int(11) NOT NULL DEFAULT '0' COMMENT '발송 성공 건수',
  `send_fail` int(11) NOT NULL DEFAULT '0' COMMENT '발송 실패 건수',
  `cr` decimal(12,2) DEFAULT NULL,
  `gmv` decimal(14,0) DEFAULT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`campaign_stat_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8



CREATE TABLE `tb_message` (
  `message_seq` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Message id',
  `service_seq` int(11) NOT NULL,
  `message_body` varchar(8192) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'message 본문. 다양한 전송 포맷 때문에 json으로 저장',
  `sys_user_seq` int(11) NOT NULL,
  `allow_target` int(11) DEFAULT '0',
  `comment` varchar(256) DEFAULT NULL,
  `is_deleted` varchar(45) NOT NULL DEFAULT 'false',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_seq`),
  KEY `fk_tb_message_tb_sys_users1_idx` (`sys_user_seq`),
  KEY `fk_tb_message_tb_services1_idx` (`service_seq`),
  CONSTRAINT `fk_tb_message_tb_services1` FOREIGN KEY (`service_seq`) REFERENCES `tb_services` (`service_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_message_tb_sys_users1` FOREIGN KEY (`sys_user_seq`) REFERENCES `tb_sys_users` (`sys_user_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1046 DEFAULT CHARSET=utf8


CREATE TABLE `tb_etl` (
  `seq` int(11) NOT NULL COMMENT 'ETL SEQ',
  `table_name` varchar(255) NOT NULL COMMENT 'ETL 대상 테이블명',
  `field_name` varchar(30) NOT NULL DEFAULT '0' COMMENT 'ETL 대상 필드명',
  `last_value` int(10) unsigned NOT NULL COMMENT 'ETL 분기 상태',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_table_name` (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='데이터 ETL 상태값 검증'
;

CREATE TABLE `tb_schedule` (
  `schedule_seq` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_name` varchar(45) NOT NULL,
  `campaign_seq` int(11) DEFAULT NULL,
  `campaign_item_seq` int(11) DEFAULT NULL,
  `minute` tinyint(4) DEFAULT NULL COMMENT 'Null 인경우 매분 실행',
  `minute_of_hour` tinyint(4) DEFAULT NULL,
  `hour` tinyint(4) DEFAULT NULL COMMENT 'null 인 경우 매 시간 실행',
  `day_of_month` tinyint(4) DEFAULT NULL COMMENT 'Null 인 경우 매일 실행',
  `month` tinyint(4) DEFAULT NULL COMMENT 'Null 인 경우 매월 실행',
  `day_of_week` tinyint(4) DEFAULT NULL COMMENT 'null 인 경우 주간 매일 실행 ',
  `start_date` datetime NOT NULL COMMENT '스케줄 시작시간. tb_campaign에 종속됨\n',
  `end_date` datetime NOT NULL COMMENT '스케줄 종료 일시. tb_campaign에 종속됨',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '0 : 대기 (최초 생성 시 대기)\n1 : 발송 대상 목록 생성 중\n2 : 발송 대기\n3 : 발송 중\n4 : 발송 중 강제 중지\n5 : 발송 완료 후 대기 (기본 대기시간 1분)\n6 : 스케줄 종료',
  `sys_user_seq` int(11) NOT NULL COMMENT '등록자',
  `is_active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '스케줄 활성화 유무 (true/false)',
  `count` int(11) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '스케줄 삭제 유무. 태스크가 완료되면 이 스케줄러는 이 플래그가 true 상태로 변경됨. 만일 동일한 캠페인인데 새로 스케줄을 시작한다면 레코드를 새로 생성해서 스케줄을 등록해야 함.',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`schedule_seq`),
  UNIQUE KEY `schedule_seq_UNIQUE` (`schedule_seq`),
  KEY `fk_tb_schedule_tb_campaign1_idx` (`campaign_seq`),
  KEY `fk_tb_schedule_tb_sys_users1_idx` (`sys_user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=853 DEFAULT CHARSET=utf8



CREATE TABLE `tb_segment` (
  `segment_seq` int(11) NOT NULL AUTO_INCREMENT,
  `segment_name` varchar(128) NOT NULL,
  `campaign_item_seq` int(11) NOT NULL,
  `sys_user_seq` int(11) NOT NULL COMMENT '작업자',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`segment_seq`),
  UNIQUE KEY `segment_seq_UNIQUE` (`segment_seq`),
  KEY `fk_tb_segment_tb_sys_users1_idx` (`sys_user_seq`),
  KEY `fk_tb_segment_tb_campaign_items1_idx` (`campaign_item_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=466 DEFAULT CHARSET=utf8


CREATE TABLE `tb_segment_detail` (
  `segment_seq` int(11) NOT NULL,
  `send_plan_datetime` datetime NOT NULL,
  `sort_no` int(10) unsigned NOT NULL,
  `token` varchar(255) NOT NULL,
  `device` tinyint(3) unsigned DEFAULT NULL,
  `mid` int(10) unsigned NOT NULL,
  `cmpn_no` smallint(5) unsigned NOT NULL,
  `cmpn_seq` smallint(5) unsigned NOT NULL,
  `test_group` smallint(5) DEFAULT NULL,
  `title` varchar(30) DEFAULT NULL,
  `content` varchar(60) DEFAULT NULL,
  `image` varchar(250) DEFAULT NULL,
  `deal_id` bigint(20) DEFAULT NULL,
  `event_landing_url` varchar(400) DEFAULT NULL,
  `utm_source` smallint(5) unsigned DEFAULT NULL,
  `utm_medium` enum('app_push','app_push_test1','app_push_test2') DEFAULT 'app_push',
  `utm_campaign` int(10) unsigned DEFAULT NULL,
  `utm_content` int(10) unsigned DEFAULT NULL,
  `allow_target` tinyint(3) unsigned DEFAULT NULL COMMENT '1: 공지/정보성 푸시(inform 테이블 기준)\n2: 광고성 푸시(spread 테이블 기준)',
  `hash` varchar(60) NOT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`token`),
  KEY `index1` (`segment_seq`),
  KEY `index2` (`sort_no`),
  KEY `index3` (`mid`,`cmpn_no`,`cmpn_seq`),
  KEY `index4` (`send_plan_datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8



CREATE TABLE `tb_send_history` (
  `schedule_seq` int(11) DEFAULT NULL COMMENT '스케줄러를 통해 발송된 경우 schedule_seq를 기록',
  `campaign_seq` int(11) NOT NULL DEFAULT '-1' COMMENT '-1 : test 발송',
  `campaign_item_seq` int(11) NOT NULL DEFAULT '-1' COMMENT '-1 : 테스트 발송',
  `message_seq` int(11) NOT NULL DEFAULT '-1' COMMENT '-1 : json raw format으로 직접 test 발송한 경우',
  `message_body` varchar(8192) NOT NULL,
  `http_code` int(11) DEFAULT NULL,
  `http_reason` varchar(45) DEFAULT NULL,
  `multicast_id` varchar(45) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `success` int(11) DEFAULT NULL,
  `failure` int(11) DEFAULT NULL,
  `canonical_ids` varchar(4096) DEFAULT NULL,
  `results` mediumtext,
  `token_seqs` mediumtext COMMENT '발송된 token_seq들에 대한 array string. ‘,’로 구분\nTest 발송인 경우는 token에 대한 array string.',
  `sys_user_seq` int(11) DEFAULT NULL,
  `start_date` datetime NOT NULL COMMENT '메세지 발송 시작 시간. 같은 메세지인 경우는 같은 시간임 ',
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_start_date` (`start_date`),
  KEY `idx_message_seq` (`message_seq`),
  KEY `idx_campaign_seq` (`campaign_seq`),
  KEY `idx_campaign_item_seq` (`campaign_item_seq`),
  KEY `idx_schedule_seq` (`schedule_seq`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE `tb_service_permission` (
  `sys_user_seq` int(11) NOT NULL,
  `service_seq` int(11) NOT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sys_user_seq`,`service_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE `tb_services` (
  `service_seq` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(45) NOT NULL DEFAULT 'none',
  `server_key` varchar(1024) NOT NULL,
  `sender_id` decimal(13,0) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '서비스 삭제 유무\nTrue : 어드민을 통해 삭제된 서비스\nFalse : 사용 가능한 서비스 ',
  `message_template` varchar(8192) NOT NULL DEFAULT '{}',
  `comment` varchar(1024) DEFAULT NULL,
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`service_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8

CREATE TABLE `tb_sys_users` (
  `sys_user_seq` int(11) NOT NULL AUTO_INCREMENT,
  `sys_user_id` varchar(45) NOT NULL,
  `sys_user_name` varchar(45) NOT NULL COMMENT '사용자 이름. 실명 기록 요망',
  `sys_user_password` varchar(255) NOT NULL,
  `sys_user_email` varchar(45) NOT NULL,
  `status` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '0 : not use\n1 : send confirm mail\n2 : user confirmed\n',
  `permission` tinyint(1) unsigned zerofill NOT NULL DEFAULT '1' COMMENT '0 : 관리자\n1 : 일반사용자\n',
  `sys_user_phone` varchar(20) NOT NULL COMMENT '사용자 전화번호. 장애발생 시 긴급 대응을 위함 ',
  `fail_cnt` tinyint(4) DEFAULT '0',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일',
  PRIMARY KEY (`sys_user_seq`),
  UNIQUE KEY `sys_user_id_UNIQUE` (`sys_user_id`),
  UNIQUE KEY `sys_user_email_UNIQUE` (`sys_user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8


CREATE TABLE `tb_user_token` (
  `token_seq` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '토큰 테이블 메인 키',
  `service_seq` int(11) NOT NULL,
  `token_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Wonder shopping table의 토큰 id. ETL 전용 \n직접 토큰을 수신한다면 사용하지 않아도 됨',
  `token` varchar(256) NOT NULL COMMENT '사용자 토큰 값',
  `is_live` tinyint(1) NOT NULL DEFAULT '1' COMMENT '사용 가능한 토큰 유무 (true/false). Default true',
  `platform` tinyint(4) NOT NULL DEFAULT '99' COMMENT '플랫폼 종류\n1 : pc\n2 : 모바일_android\n3 : 모바일_ios\n4 : 모바일_windows 폰 계열\n5 : 모바일_android tablet\n6 : 모바일_ios tablet\n7 : 모바일_windows tablet\n99: ETC\n',
  `device_id` varchar(64) NOT NULL COMMENT 'Device id',
  `device_name` varchar(256) DEFAULT NULL COMMENT '디바이스 명. \npc인 경우는 브라우저 명.\n모바일은 디바이스명 또는 모델명 ',
  `mid` int(11) NOT NULL DEFAULT '0',
  `user_agent` varchar(256) DEFAULT NULL,
  `update_at` int(11) DEFAULT '0' COMMENT '원더쇼핑 etl용. 직접 토큰 수신한다면 사용하지 않아도 됨',
  `comment` varchar(256) DEFAULT NULL,
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '최초 등록일',
  `mod_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일. 토큰이 수정될 일이 있으면 is_live가 false가 되고 이 날짜만 수정된다.',
  PRIMARY KEY (`token_seq`),
  UNIQUE KEY `token_UNIQUE` (`token`),
  KEY `fk_tb_user_token_tb_services1_idx` (`service_seq`),
  KEY `idx_token_id` (`token_id`),
  KEY `update_at` (`update_at`),
  KEY `idx_mid` (`mid`),
  CONSTRAINT `fk_tb_user_token_tb_services1` FOREIGN KEY (`service_seq`) REFERENCES `tb_services` (`service_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17704609 DEFAULT CHARSET=utf8


CREATE TABLE `tb_user_token_target` (
  `token_seq` int(11) NOT NULL,
  `service_seq` int(11) NOT NULL,
  `allow_target` int(11) NOT NULL COMMENT '1: 공지 푸시\n2: 광고성 푸시\n3: 정보성 푸시\n4: 이후부터는 서비스별로 재정의 될 수도 있음 ',
  `is_live` tinyint(1) DEFAULT '0' COMMENT 'True: allow_target으로 전송 가능\nFalse : allow_target으로 전송 하지 않음\n',
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `mod_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`token_seq`,`service_seq`,`allow_target`),
  KEY `idx_serviceToken` (`token_seq`,`service_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
