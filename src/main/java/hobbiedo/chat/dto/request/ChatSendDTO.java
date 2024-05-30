package hobbiedo.chat.vo.request;

import lombok.Getter;

@Getter
public class ChatSendVo {
	private Long crewId;
	private String text;
	private String imageUrl;
	private String videoUrl;
	private String entryExitNotice;
}
