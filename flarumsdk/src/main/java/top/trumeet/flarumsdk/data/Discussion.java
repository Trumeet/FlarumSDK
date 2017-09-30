package top.trumeet.flarumsdk.data;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("discussions")
public class Discussion extends Resource {
	private String lastTime;
	private int participantsCount;
	private int lastPostNumber;
	private boolean canReply;
	private boolean canSplit;
	private boolean canRename;
	private String title;
	private boolean canHide;
	private boolean isSticky;
	private int startUserId;
	private boolean canTag;
	private boolean canSticky;
	private int commentsCount;
	private boolean isLocked;
	private String startTime;
	private boolean canDelete;
	private boolean canLock;
	private boolean isApproved;
	private boolean canSelectBestAnswer;
	private String slug;

	public void setLastTime(String lastTime){
		this.lastTime = lastTime;
	}

	public String getLastTime(){
		return lastTime;
	}

	public void setParticipantsCount(int participantsCount){
		this.participantsCount = participantsCount;
	}

	public int getParticipantsCount(){
		return participantsCount;
	}

	public void setLastPostNumber(int lastPostNumber){
		this.lastPostNumber = lastPostNumber;
	}

	public int getLastPostNumber(){
		return lastPostNumber;
	}

	public void setCanReply(boolean canReply){
		this.canReply = canReply;
	}

	public boolean isCanReply(){
		return canReply;
	}

	public void setCanSplit(boolean canSplit){
		this.canSplit = canSplit;
	}

	public boolean isCanSplit(){
		return canSplit;
	}

	public void setCanRename(boolean canRename){
		this.canRename = canRename;
	}

	public boolean isCanRename(){
		return canRename;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setCanHide(boolean canHide){
		this.canHide = canHide;
	}

	public boolean isCanHide(){
		return canHide;
	}

	public void setIsSticky(boolean isSticky){
		this.isSticky = isSticky;
	}

	public boolean isIsSticky(){
		return isSticky;
	}

	public void setStartUserId(int startUserId){
		this.startUserId = startUserId;
	}

	public int getStartUserId(){
		return startUserId;
	}

	public void setCanTag(boolean canTag){
		this.canTag = canTag;
	}

	public boolean isCanTag(){
		return canTag;
	}

	public void setCanSticky(boolean canSticky){
		this.canSticky = canSticky;
	}

	public boolean isCanSticky(){
		return canSticky;
	}

	public void setCommentsCount(int commentsCount){
		this.commentsCount = commentsCount;
	}

	public int getCommentsCount(){
		return commentsCount;
	}

	public void setIsLocked(boolean isLocked){
		this.isLocked = isLocked;
	}

	public boolean isIsLocked(){
		return isLocked;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setCanDelete(boolean canDelete){
		this.canDelete = canDelete;
	}

	public boolean isCanDelete(){
		return canDelete;
	}

	public void setCanLock(boolean canLock){
		this.canLock = canLock;
	}

	public boolean isCanLock(){
		return canLock;
	}

	public void setIsApproved(boolean isApproved){
		this.isApproved = isApproved;
	}

	public boolean isIsApproved(){
		return isApproved;
	}

	public void setCanSelectBestAnswer(boolean canSelectBestAnswer){
		this.canSelectBestAnswer = canSelectBestAnswer;
	}

	public boolean isCanSelectBestAnswer(){
		return canSelectBestAnswer;
	}

	public void setSlug(String slug){
		this.slug = slug;
	}

	public String getSlug(){
		return slug;
	}

	@Override
 	public String toString(){
		return 
			"Discussion{" + 
			"lastTime = '" + lastTime + '\'' + 
			",participantsCount = '" + participantsCount + '\'' + 
			",lastPostNumber = '" + lastPostNumber + '\'' + 
			",canReply = '" + canReply + '\'' + 
			",canSplit = '" + canSplit + '\'' + 
			",canRename = '" + canRename + '\'' + 
			",title = '" + title + '\'' + 
			",canHide = '" + canHide + '\'' + 
			",isSticky = '" + isSticky + '\'' + 
			",startUserId = '" + startUserId + '\'' + 
			",canTag = '" + canTag + '\'' + 
			",canSticky = '" + canSticky + '\'' + 
			",commentsCount = '" + commentsCount + '\'' + 
			",isLocked = '" + isLocked + '\'' + 
			",startTime = '" + startTime + '\'' + 
			",canDelete = '" + canDelete + '\'' + 
			",canLock = '" + canLock + '\'' + 
			",isApproved = '" + isApproved + '\'' + 
			",canSelectBestAnswer = '" + canSelectBestAnswer + '\'' + 
			",slug = '" + slug + '\'' + 
			"}";
		}
}
