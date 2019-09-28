package top.death00.dis.schedule.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author death00
 * @date 2019/9/24 18:48
 */
@Document
@Getter
@Setter
public class ServerConfig {

	@Id
	private ObjectId id;

	/**
	 * 服务的名称
	 */
	@Indexed(unique = true, background = true)
	private String name;

	public static final String NAME = "name";

	/**
	 * 是否存活
	 */
	private boolean alive;

	public static final String ALIVE = "alive";

	private Date createTimestamp;

	public static final String CREATE_TIMESTAMP = "createTimestamp";

	private Date updateTimestamp;

	public static final String UPDATE_TIMESTAMP = "updateTimestamp";
}
