package top.trumeet.flarumsdk.data;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("forums")
public class Forum extends Resource {

	private String guidelinesUrl;

	private String headerHtml;

	private boolean debug;

	private String welcomeTitle;

	private boolean canViewDiscussions;

	private boolean allowSignUp;

	private String welcomeMessage;

	private boolean canViewFlags;

	private String description;

	private String themePrimaryColor;

	private String maxSecondaryTags;

	private String title;

	private String defaultRoute;

	private String logoUrl;

	private String minSecondaryTags;

	private String baseUrl;

	private String faviconUrl;

	private String basePath;

	private String apiUrl;

	private boolean canStartDiscussion;

	private String themeSecondaryColor;

	private String maxPrimaryTags;

	private String minPrimaryTags;

	public String getGuidelinesUrl() {
		return guidelinesUrl;
	}

	public void setGuidelinesUrl(String guidelinesUrl) {
		this.guidelinesUrl = guidelinesUrl;
	}

	public String getHeaderHtml() {
		return headerHtml;
	}

	public void setHeaderHtml(String headerHtml) {
		this.headerHtml = headerHtml;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getWelcomeTitle() {
		return welcomeTitle;
	}

	public void setWelcomeTitle(String welcomeTitle) {
		this.welcomeTitle = welcomeTitle;
	}

	public boolean isCanViewDiscussions() {
		return canViewDiscussions;
	}

	public void setCanViewDiscussions(boolean canViewDiscussions) {
		this.canViewDiscussions = canViewDiscussions;
	}

	public boolean isAllowSignUp() {
		return allowSignUp;
	}

	public void setAllowSignUp(boolean allowSignUp) {
		this.allowSignUp = allowSignUp;
	}

	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public boolean isCanViewFlags() {
		return canViewFlags;
	}

	public void setCanViewFlags(boolean canViewFlags) {
		this.canViewFlags = canViewFlags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThemePrimaryColor() {
		return themePrimaryColor;
	}

	public void setThemePrimaryColor(String themePrimaryColor) {
		this.themePrimaryColor = themePrimaryColor;
	}

	public String getMaxSecondaryTags() {
		return maxSecondaryTags;
	}

	public void setMaxSecondaryTags(String maxSecondaryTags) {
		this.maxSecondaryTags = maxSecondaryTags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDefaultRoute() {
		return defaultRoute;
	}

	public void setDefaultRoute(String defaultRoute) {
		this.defaultRoute = defaultRoute;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getMinSecondaryTags() {
		return minSecondaryTags;
	}

	public void setMinSecondaryTags(String minSecondaryTags) {
		this.minSecondaryTags = minSecondaryTags;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getFaviconUrl() {
		return faviconUrl;
	}

	public void setFaviconUrl(String faviconUrl) {
		this.faviconUrl = faviconUrl;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public boolean isCanStartDiscussion() {
		return canStartDiscussion;
	}

	public void setCanStartDiscussion(boolean canStartDiscussion) {
		this.canStartDiscussion = canStartDiscussion;
	}

	public String getThemeSecondaryColor() {
		return themeSecondaryColor;
	}

	public void setThemeSecondaryColor(String themeSecondaryColor) {
		this.themeSecondaryColor = themeSecondaryColor;
	}

	public String getMaxPrimaryTags() {
		return maxPrimaryTags;
	}

	public void setMaxPrimaryTags(String maxPrimaryTags) {
		this.maxPrimaryTags = maxPrimaryTags;
	}

	public String getMinPrimaryTags() {
		return minPrimaryTags;
	}

	public void setMinPrimaryTags(String minPrimaryTags) {
		this.minPrimaryTags = minPrimaryTags;
	}

	@Override
	public String toString() {
		return "Forum{" +
				"guidelinesUrl='" + guidelinesUrl + '\'' +
				", headerHtml='" + headerHtml + '\'' +
				", debug=" + debug +
				", welcomeTitle='" + welcomeTitle + '\'' +
				", canViewDiscussions=" + canViewDiscussions +
				", allowSignUp=" + allowSignUp +
				", welcomeMessage='" + welcomeMessage + '\'' +
				", canViewFlags=" + canViewFlags +
				", description='" + description + '\'' +
				", themePrimaryColor='" + themePrimaryColor + '\'' +
				", maxSecondaryTags='" + maxSecondaryTags + '\'' +
				", title='" + title + '\'' +
				", defaultRoute='" + defaultRoute + '\'' +
				", logoUrl='" + logoUrl + '\'' +
				", minSecondaryTags='" + minSecondaryTags + '\'' +
				", baseUrl='" + baseUrl + '\'' +
				", faviconUrl='" + faviconUrl + '\'' +
				", basePath='" + basePath + '\'' +
				", apiUrl='" + apiUrl + '\'' +
				", canStartDiscussion=" + canStartDiscussion +
				", themeSecondaryColor='" + themeSecondaryColor + '\'' +
				", maxPrimaryTags='" + maxPrimaryTags + '\'' +
				", minPrimaryTags='" + minPrimaryTags + '\'' +
				'}';
	}
}