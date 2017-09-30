package top.trumeet.flarumsdk.data;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("tags")
public class Tag extends Resource {
	private Object backgroundUrl;
	private String lastTime;
	private Object backgroundMode;
	private String color;
	private String description;
	private Object defaultSort;
	private boolean isHidden;
	private int discussionsCount;
	private boolean canAddToDiscussion;
	private boolean canStartDiscussion;
	private String name;
	private Object iconUrl;
	private int position;
	private boolean isRestricted;
	private String slug;
	private boolean isChild;

	public void setBackgroundUrl(Object backgroundUrl){
		this.backgroundUrl = backgroundUrl;
	}

	public Object getBackgroundUrl(){
		return backgroundUrl;
	}

	public void setLastTime(String lastTime){
		this.lastTime = lastTime;
	}

	public String getLastTime(){
		return lastTime;
	}

	public void setBackgroundMode(Object backgroundMode){
		this.backgroundMode = backgroundMode;
	}

	public Object getBackgroundMode(){
		return backgroundMode;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setDefaultSort(Object defaultSort){
		this.defaultSort = defaultSort;
	}

	public Object getDefaultSort(){
		return defaultSort;
	}

	public void setIsHidden(boolean isHidden){
		this.isHidden = isHidden;
	}

	public boolean isIsHidden(){
		return isHidden;
	}

	public void setDiscussionsCount(int discussionsCount){
		this.discussionsCount = discussionsCount;
	}

	public int getDiscussionsCount(){
		return discussionsCount;
	}

	public void setCanAddToDiscussion(boolean canAddToDiscussion){
		this.canAddToDiscussion = canAddToDiscussion;
	}

	public boolean isCanAddToDiscussion(){
		return canAddToDiscussion;
	}

	public void setCanStartDiscussion(boolean canStartDiscussion){
		this.canStartDiscussion = canStartDiscussion;
	}

	public boolean isCanStartDiscussion(){
		return canStartDiscussion;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIconUrl(Object iconUrl){
		this.iconUrl = iconUrl;
	}

	public Object getIconUrl(){
		return iconUrl;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getPosition(){
		return position;
	}

	public void setIsRestricted(boolean isRestricted){
		this.isRestricted = isRestricted;
	}

	public boolean isIsRestricted(){
		return isRestricted;
	}

	public void setSlug(String slug){
		this.slug = slug;
	}

	public String getSlug(){
		return slug;
	}

	public void setIsChild(boolean isChild){
		this.isChild = isChild;
	}

	public boolean isIsChild(){
		return isChild;
	}

	@Override
 	public String toString(){
		return 
			"Tag{" + 
			"backgroundUrl = '" + backgroundUrl + '\'' + 
			",lastTime = '" + lastTime + '\'' + 
			",backgroundMode = '" + backgroundMode + '\'' + 
			",color = '" + color + '\'' + 
			",description = '" + description + '\'' + 
			",defaultSort = '" + defaultSort + '\'' + 
			",isHidden = '" + isHidden + '\'' + 
			",discussionsCount = '" + discussionsCount + '\'' + 
			",canAddToDiscussion = '" + canAddToDiscussion + '\'' + 
			",canStartDiscussion = '" + canStartDiscussion + '\'' + 
			",name = '" + name + '\'' + 
			",iconUrl = '" + iconUrl + '\'' + 
			",position = '" + position + '\'' + 
			",isRestricted = '" + isRestricted + '\'' + 
			",slug = '" + slug + '\'' + 
			",isChild = '" + isChild + '\'' + 
			"}";
		}
}
