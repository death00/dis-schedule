package top.death00.dis.schedule.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author death00
 * @date 2019/9/24 18:47
 */
@CompoundIndexes({
	@CompoundIndex(
		name = "date_1_name_1",
		def = "{'date' : 1, 'name' : 1}",
		unique = true,
		background = true
	)
})
@Document
@Getter
@Setter
public class DisScheduleRecord {

	@Id
	private ObjectId id;

	/**
	 * 定时调度任务的名称
	 */
	private String name;

	public static final String NAME = "name";

	/**
	 * 定时调度任务所属的任务时间
	 */
	private Date date;

	public static final String DATE = "date";

	/**
	 * 日期
	 */
	private String dateStr;

	public static final String DATE_STR = "dateStr";

	/**
	 * 执行定时调度的服务器名称
	 */
	private String serverName;

	public static final String SERVER_NAME = "serverName";

	/**
	 * 记录创建时间
	 */
	private Date createTimestamp;

	public static final String CREATE_TIMESTAMP = "createTimestamp";

	/**
	 * 记录更新时间
	 */
	private Date updateTimestamp;

	public static final String UPDATE_TIMESTAMP = "updateTimestamp";
}

