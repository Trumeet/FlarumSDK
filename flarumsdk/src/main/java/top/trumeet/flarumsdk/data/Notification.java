package top.trumeet.flarumsdk.data;


import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("notifications")
public class Notification extends Resource {

	private boolean isRead;

	private int id;

	private String time;

	private String contentType;

	private Object content;

	public void setIsRead(boolean isRead){
		this.isRead = isRead;
	}

	public boolean isIsRead(){
		return isRead;
	}

	public void setMsgId(int id){
		this.id = id;
	}

	public int getMsgId(){
		return id;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public String getContentType(){
		return contentType;
	}

	public void setContent(Object content){
		this.content = content;
	}

	public Object getContent(){
		return content;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"isRead = '" + isRead + '\'' + 
			",id = '" + id + '\'' + 
			",time = '" + time + '\'' + 
			",contentType = '" + contentType + '\'' + 
			",content = '" + content + '\'' + 
			"}";
		}
}