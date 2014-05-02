package com.moto.inform;

public class ChatEntity {
    
	private int userImage;
	private String content;
	private String chatTime;
	private boolean isComeMsg;
    private String utcTimeStamp;

    public int getUserImage() {
		return userImage;
	}
	public void setUserImage(int userImage) {
		this.userImage = userImage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public boolean isComeMsg() {
		return isComeMsg;
	}
	public void setComeMsg(boolean isComeMsg) {
		this.isComeMsg = isComeMsg;
	}
	public void setUtcTimeStamp(String timeStamp)
    {
        this.utcTimeStamp = timeStamp;
    }
    public String getUtcTimeStamp()
    {
        return this.utcTimeStamp;
    }
}
