package com.tingyouqu.server.dao.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_user_message_record
 * @author 
 */
@Data
public class TUserMessageRecord implements Serializable {
    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private String messageId;

    private Long messageTimestamp;

    private String ext;

    private Date createTime;

    private Date updateTime;

    private Integer ackState;

    private static final long serialVersionUID = 1L;
}