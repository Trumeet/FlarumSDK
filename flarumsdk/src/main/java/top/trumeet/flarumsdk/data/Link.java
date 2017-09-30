package top.trumeet.flarumsdk.data;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("link")
public class Link extends Resource {
	private boolean isInternal;
	private boolean isNewtab;
	private int id;
	private int position;
	private String title;
	private String url;

	public void setIsInternal(boolean isInternal){
		this.isInternal = isInternal;
	}

	public boolean isIsInternal(){
		return isInternal;
	}

	public void setIsNewtab(boolean isNewtab){
		this.isNewtab = isNewtab;
	}

	public boolean isIsNewtab(){
		return isNewtab;
	}

	public void setLinkId(int id){
		this.id = id;
	}

	public int getLinkId(){
		return id;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getPosition(){
		return position;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"Link{" + 
			"isInternal = '" + isInternal + '\'' + 
			",isNewtab = '" + isNewtab + '\'' + 
			",id = '" + id + '\'' + 
			",position = '" + position + '\'' + 
			",title = '" + title + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}
