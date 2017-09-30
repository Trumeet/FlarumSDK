package top.trumeet.flarumsdk.data;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("groups")
public class Group extends Resource {
	private String namePlural;
	private String color;
	private String icon;
	private String nameSingular;

	public void setNamePlural(String namePlural){
		this.namePlural = namePlural;
	}

	public String getNamePlural(){
		return namePlural;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return icon;
	}

	public void setNameSingular(String nameSingular){
		this.nameSingular = nameSingular;
	}

	public String getNameSingular(){
		return nameSingular;
	}

	@Override
 	public String toString(){
		return 
			"Group{" + 
			"namePlural = '" + namePlural + '\'' + 
			",color = '" + color + '\'' + 
			",icon = '" + icon + '\'' + 
			",nameSingular = '" + nameSingular + '\'' + 
			"}";
		}
}
