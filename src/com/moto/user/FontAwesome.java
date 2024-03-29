package com.moto.user;

import java.util.HashMap;
import java.util.Map;

public class FontAwesome {

	private static Map<String, String> faMap = new HashMap<String, String>();

	//font awesome map as per 
	//http://fortawesome.github.io/Font-Awesome/cheatsheet/
	
	static {
		faMap.put("fa-glass", "\uf000");
		faMap.put("fa-music", "\uf001");
		faMap.put("fa-search", "\uf002");
		faMap.put("fa-envelope-o", "\uf003");
		faMap.put("fa-heart", "\uf004");
		faMap.put("fa-star", "\uf005");
		faMap.put("fa-star-o", "\uf006");
		faMap.put("fa-user", "\uf007");
		faMap.put("fa-film", "\uf008");
		faMap.put("fa-th-large", "\uf009");
		faMap.put("fa-th", "\uf00a");
		faMap.put("fa-th-list", "\uf00b");
		faMap.put("fa-check", "\uf00c");
		faMap.put("fa-times", "\uf00d");
		faMap.put("fa-search-plus", "\uf00e");
		faMap.put("fa-search-minus", "\uf010");
		faMap.put("fa-power-off", "\uf011");
		faMap.put("fa-signal", "\uf012");
		faMap.put("fa-cog", "\uf013");
		faMap.put("fa-trash-o", "\uf014");
		faMap.put("fa-home", "\uf015");
		faMap.put("fa-file-o", "\uf016");
		faMap.put("fa-clock-o", "\uf017");
		faMap.put("fa-road", "\uf018");
		faMap.put("fa-download", "\uf019");
		faMap.put("fa-arrow-circle-o-down", "\uf01a");
		faMap.put("fa-arrow-circle-o-up", "\uf01b");
		faMap.put("fa-inbox", "\uf01c");
		faMap.put("fa-play-circle-o", "\uf01d");
		faMap.put("fa-repeat", "\uf01e");
		faMap.put("fa-refresh", "\uf021");
		faMap.put("fa-list-alt", "\uf022");
		faMap.put("fa-lock", "\uf023");
		faMap.put("fa-flag", "\uf024");
		faMap.put("fa-headphones", "\uf025");
		faMap.put("fa-volume-off", "\uf026");
		faMap.put("fa-volume-down", "\uf027");
		faMap.put("fa-volume-up", "\uf028");
		faMap.put("fa-qrcode", "\uf029");
		faMap.put("fa-barcode", "\uf02a");
		faMap.put("fa-tag", "\uf02b");
		faMap.put("fa-tags", "\uf02c");
		faMap.put("fa-book", "\uf02d");
		faMap.put("fa-bookmark", "\uf02e");
		faMap.put("fa-print", "\uf02f");
		faMap.put("fa-camera", "\uf030");
		faMap.put("fa-font", "\uf031");
		faMap.put("fa-bold", "\uf032");
		faMap.put("fa-italic", "\uf033");
		faMap.put("fa-text-height", "\uf034");
		faMap.put("fa-text-width", "\uf035");
		faMap.put("fa-align-left", "\uf036");
		faMap.put("fa-align-center", "\uf037");
		faMap.put("fa-align-right", "\uf038");
		faMap.put("fa-align-justify", "\uf039");
		faMap.put("fa-list", "\uf03a");
		faMap.put("fa-outdent", "\uf03b");
		faMap.put("fa-indent", "\uf03c");
		faMap.put("fa-video-camera", "\uf03d");
		faMap.put("fa-picture-o", "\uf03e");
		faMap.put("fa-pencil", "\uf040");
		faMap.put("fa-map-marker", "\uf041");
		faMap.put("fa-adjust", "\uf042");
		faMap.put("fa-tint", "\uf043");
		faMap.put("fa-pencil-square-o", "\uf044");
		faMap.put("fa-share-square-o", "\uf045");
		faMap.put("fa-check-square-o", "\uf046");
		faMap.put("fa-move", "\uf047");
		faMap.put("fa-step-backward", "\uf048");
		faMap.put("fa-fast-backward", "\uf049");
		faMap.put("fa-backward", "\uf04a");
		faMap.put("fa-play", "\uf04b");
		faMap.put("fa-pause", "\uf04c");
		faMap.put("fa-stop", "\uf04d");
		faMap.put("fa-forward", "\uf04e");
		faMap.put("fa-fast-forward", "\uf050");
		faMap.put("fa-step-forward", "\uf051");
		faMap.put("fa-eject", "\uf052");
		faMap.put("fa-chevron-left", "\uf053");
		faMap.put("fa-chevron-right", "\uf054");
		faMap.put("fa-plus-circle", "\uf055");
		faMap.put("fa-minus-circle", "\uf056");
		faMap.put("fa-times-circle", "\uf057");
		faMap.put("fa-check-circle", "\uf058");
		faMap.put("fa-question-circle", "\uf059");
		faMap.put("fa-info-circle", "\uf05a");
		faMap.put("fa-crosshairs", "\uf05b");
		faMap.put("fa-times-circle-o", "\uf05c");
		faMap.put("fa-check-circle-o", "\uf05d");
		faMap.put("fa-ban", "\uf05e");
		faMap.put("fa-arrow-left", "\uf060");
		faMap.put("fa-arrow-right", "\uf061");
		faMap.put("fa-arrow-up", "\uf062");
		faMap.put("fa-arrow-down", "\uf063");
		faMap.put("fa-share", "\uf064");
		faMap.put("fa-resize-full", "\uf065");
		faMap.put("fa-resize-small", "\uf066");
		faMap.put("fa-plus", "\uf067");
		faMap.put("fa-minus", "\uf068");
		faMap.put("fa-asterisk", "\uf069");
		faMap.put("fa-exclamation-circle", "\uf06a");
		faMap.put("fa-gift", "\uf06b");
		faMap.put("fa-leaf", "\uf06c");
		faMap.put("fa-fire", "\uf06d");
		faMap.put("fa-eye", "\uf06e");
		faMap.put("fa-eye-slash", "\uf070");
		faMap.put("fa-exclamation-triangle", "\uf071");
		faMap.put("fa-plane", "\uf072");
		faMap.put("fa-calendar", "\uf073");
		faMap.put("fa-random", "\uf074");
		faMap.put("fa-comment", "\uf075");
		faMap.put("fa-magnet", "\uf076");
		faMap.put("fa-chevron-up", "\uf077");
		faMap.put("fa-chevron-down", "\uf078");
		faMap.put("fa-retweet", "\uf079");
		faMap.put("fa-shopping-cart", "\uf07a");
		faMap.put("fa-folder", "\uf07b");
		faMap.put("fa-folder-open", "\uf07c");
		faMap.put("fa-resize-vertical", "\uf07d");
		faMap.put("fa-resize-horizontal", "\uf07e");
		faMap.put("fa-bar-chart-o", "\uf080");
		faMap.put("fa-twitter-square", "\uf081");
		faMap.put("fa-facebook-square", "\uf082");
		faMap.put("fa-camera-retro", "\uf083");
		faMap.put("fa-key", "\uf084");
		faMap.put("fa-cogs", "\uf085");
		faMap.put("fa-comments", "\uf086");
		faMap.put("fa-thumbs-o-up", "\uf087");
		faMap.put("fa-thumbs-o-down", "\uf088");
		faMap.put("fa-star-half", "\uf089");
		faMap.put("fa-heart-o", "\uf08a");
		faMap.put("fa-sign-out", "\uf08b");
		faMap.put("fa-linkedin-square", "\uf08c");
		faMap.put("fa-thumb-tack", "\uf08d");
		faMap.put("fa-external-link", "\uf08e");
		faMap.put("fa-sign-in", "\uf090");
		faMap.put("fa-trophy", "\uf091");
		faMap.put("fa-github-square", "\uf092");
		faMap.put("fa-upload", "\uf093");
		faMap.put("fa-lemon-o", "\uf094");
		faMap.put("fa-phone", "\uf095");
		faMap.put("fa-square-o", "\uf096");
		faMap.put("fa-bookmark-o", "\uf097");
		faMap.put("fa-phone-square", "\uf098");
		faMap.put("fa-twitter", "\uf099");
		faMap.put("fa-facebook", "\uf09a");
		faMap.put("fa-github", "\uf09b");
		faMap.put("fa-unlock", "\uf09c");
		faMap.put("fa-credit-card", "\uf09d");
		faMap.put("fa-rss", "\uf09e");
		faMap.put("fa-hdd", "\uf0a0");
		faMap.put("fa-bullhorn", "\uf0a1");
		faMap.put("fa-bell", "\uf0f3");
		faMap.put("fa-certificate", "\uf0a3");
		faMap.put("fa-hand-o-right", "\uf0a4");
		faMap.put("fa-hand-o-left", "\uf0a5");
		faMap.put("fa-hand-o-up", "\uf0a6");
		faMap.put("fa-hand-o-down", "\uf0a7");
		faMap.put("fa-arrow-circle-left", "\uf0a8");
		faMap.put("fa-arrow-circle-right", "\uf0a9");
		faMap.put("fa-arrow-circle-up", "\uf0aa");
		faMap.put("fa-arrow-circle-down", "\uf0ab");
		faMap.put("fa-globe", "\uf0ac");
		faMap.put("fa-wrench", "\uf0ad");
		faMap.put("fa-tasks", "\uf0ae");
		faMap.put("fa-filter", "\uf0b0");
		faMap.put("fa-briefcase", "\uf0b1");
		faMap.put("fa-fullscreen", "\uf0b2");
		faMap.put("fa-group", "\uf0c0");
		faMap.put("fa-link", "\uf0c1");
		faMap.put("fa-cloud", "\uf0c2");
		faMap.put("fa-flask", "\uf0c3");
		faMap.put("fa-scissors", "\uf0c4");
		faMap.put("fa-files-o", "\uf0c5");
		faMap.put("fa-paperclip", "\uf0c6");
		faMap.put("fa-floppy-o", "\uf0c7");
		faMap.put("fa-square", "\uf0c8");
		faMap.put("fa-reorder", "\uf0c9");
		faMap.put("fa-list-ul", "\uf0ca");
		faMap.put("fa-list-ol", "\uf0cb");
		faMap.put("fa-strikethrough", "\uf0cc");
		faMap.put("fa-underline", "\uf0cd");
		faMap.put("fa-table", "\uf0ce");
		faMap.put("fa-magic", "\uf0d0");
		faMap.put("fa-truck", "\uf0d1");
		faMap.put("fa-pinterest", "\uf0d2");
		faMap.put("fa-pinterest-square", "\uf0d3");
		faMap.put("fa-google-plus-square", "\uf0d4");
		faMap.put("fa-google-plus", "\uf0d5");
		faMap.put("fa-money", "\uf0d6");
		faMap.put("fa-caret-down", "\uf0d7");
		faMap.put("fa-caret-up", "\uf0d8");
		faMap.put("fa-caret-left", "\uf0d9");
		faMap.put("fa-caret-right", "\uf0da");
		faMap.put("fa-columns", "\uf0db");
		faMap.put("fa-sort", "\uf0dc");
		faMap.put("fa-sort-asc", "\uf0dd");
		faMap.put("fa-sort-desc", "\uf0de");
		faMap.put("fa-envelope", "\uf0e0");
		faMap.put("fa-linkedin", "\uf0e1");
		faMap.put("fa-undo", "\uf0e2");
		faMap.put("fa-gavel", "\uf0e3");
		faMap.put("fa-tachometer", "\uf0e4");
		faMap.put("fa-comment-o", "\uf0e5");
		faMap.put("fa-comments-o", "\uf0e6");
		faMap.put("fa-bolt", "\uf0e7");
		faMap.put("fa-sitemap", "\uf0e8");
		faMap.put("fa-umbrella", "\uf0e9");
		faMap.put("fa-clipboard", "\uf0ea");
		faMap.put("fa-lightbulb-o", "\uf0eb");
		faMap.put("fa-exchange", "\uf0ec");
		faMap.put("fa-cloud-download", "\uf0ed");
		faMap.put("fa-cloud-upload", "\uf0ee");
		faMap.put("fa-user-md", "\uf0f0");
		faMap.put("fa-stethoscope", "\uf0f1");
		faMap.put("fa-suitcase", "\uf0f2");
		faMap.put("fa-bell-o", "\uf0a2");
		faMap.put("fa-coffee", "\uf0f4");
		faMap.put("fa-cutlery", "\uf0f5");
		faMap.put("fa-file-text-o", "\uf0f6");
		faMap.put("fa-building", "\uf0f7");
		faMap.put("fa-hospital", "\uf0f8");
		faMap.put("fa-ambulance", "\uf0f9");
		faMap.put("fa-medkit", "\uf0fa");
		faMap.put("fa-fighter-jet", "\uf0fb");
		faMap.put("fa-beer", "\uf0fc");
		faMap.put("fa-h-square", "\uf0fd");
		faMap.put("fa-plus-square", "\uf0fe");
		faMap.put("fa-angle-double-left", "\uf100");
		faMap.put("fa-angle-double-right", "\uf101");
		faMap.put("fa-angle-double-up", "\uf102");
		faMap.put("fa-angle-double-down", "\uf103");
		faMap.put("fa-angle-left", "\uf104");
		faMap.put("fa-angle-right", "\uf105");
		faMap.put("fa-angle-up", "\uf106");
		faMap.put("fa-angle-down", "\uf107");
		faMap.put("fa-desktop", "\uf108");
		faMap.put("fa-laptop", "\uf109");
		faMap.put("fa-tablet", "\uf10a");
		faMap.put("fa-mobile", "\uf10b");
		faMap.put("fa-circle-o", "\uf10c");
		faMap.put("fa-quote-left", "\uf10d");
		faMap.put("fa-quote-right", "\uf10e");
		faMap.put("fa-spinner", "\uf110");
		faMap.put("fa-circle", "\uf111");
		faMap.put("fa-reply", "\uf112");
		faMap.put("fa-github-alt", "\uf113");
		faMap.put("fa-folder-o", "\uf114");
		faMap.put("fa-folder-open-o", "\uf115");
		faMap.put("fa-expand-o", "\uf116");
		faMap.put("fa-collapse-o", "\uf117");
		faMap.put("fa-smile-o", "\uf118");
		faMap.put("fa-frown-o", "\uf119");
		faMap.put("fa-meh-o", "\uf11a");
		faMap.put("fa-gamepad", "\uf11b");
		faMap.put("fa-keyboard-o", "\uf11c");
		faMap.put("fa-flag-o", "\uf11d");
		faMap.put("fa-flag-checkered", "\uf11e");
		faMap.put("fa-terminal", "\uf120");
		faMap.put("fa-code", "\uf121");
		faMap.put("fa-reply-all", "\uf122");
		faMap.put("fa-mail-reply-all", "\uf122");
		faMap.put("fa-star-half-o", "\uf123");
		faMap.put("fa-location-arrow", "\uf124");
		faMap.put("fa-crop", "\uf125");
		faMap.put("fa-code-fork", "\uf126");
		faMap.put("fa-chain-broken", "\uf127");
		faMap.put("fa-question", "\uf128");
		faMap.put("fa-info", "\uf129");
		faMap.put("fa-exclamation", "\uf12a");
		faMap.put("fa-superscript", "\uf12b");
		faMap.put("fa-subscript", "\uf12c");
		faMap.put("fa-eraser", "\uf12d");
		faMap.put("fa-puzzle-piece", "\uf12e");
		faMap.put("fa-microphone", "\uf130");
		faMap.put("fa-microphone-slash", "\uf131");
		faMap.put("fa-shield", "\uf132");
		faMap.put("fa-calendar-o", "\uf133");
		faMap.put("fa-fire-extinguisher", "\uf134");
		faMap.put("fa-rocket", "\uf135");
		faMap.put("fa-maxcdn", "\uf136");
		faMap.put("fa-chevron-circle-left", "\uf137");
		faMap.put("fa-chevron-circle-right", "\uf138");
		faMap.put("fa-chevron-circle-up", "\uf139");
		faMap.put("fa-chevron-circle-down", "\uf13a");
		faMap.put("fa-html5", "\uf13b");
		faMap.put("fa-css3", "\uf13c");
		faMap.put("fa-anchor", "\uf13d");
		faMap.put("fa-unlock-o", "\uf13e");
		faMap.put("fa-bullseye", "\uf140");
		faMap.put("fa-ellipsis-horizontal", "\uf141");
		faMap.put("fa-ellipsis-vertical", "\uf142");
		faMap.put("fa-rss-square", "\uf143");
		faMap.put("fa-play-circle", "\uf144");
		faMap.put("fa-ticket", "\uf145");
		faMap.put("fa-minus-square", "\uf146");
		faMap.put("fa-minus-square-o", "\uf147");
		faMap.put("fa-level-up", "\uf148");
		faMap.put("fa-level-down", "\uf149");
		faMap.put("fa-check-square", "\uf14a");
		faMap.put("fa-pencil-square", "\uf14b");
		faMap.put("fa-external-link-square", "\uf14c");
		faMap.put("fa-share-square", "\uf14d");
		faMap.put("fa-compass", "\uf14e");
		faMap.put("fa-caret-square-o-down", "\uf150");
		faMap.put("fa-caret-square-o-up", "\uf151");
		faMap.put("fa-caret-square-o-right", "\uf152");
		faMap.put("fa-eur", "\uf153");
		faMap.put("fa-gbp", "\uf154");
		faMap.put("fa-usd", "\uf155");
		faMap.put("fa-inr", "\uf156");
		faMap.put("fa-jpy", "\uf157");
		faMap.put("fa-rub", "\uf158");
		faMap.put("fa-krw", "\uf159");
		faMap.put("fa-btc", "\uf15a");
		faMap.put("fa-file", "\uf15b");
		faMap.put("fa-file-text", "\uf15c");
		faMap.put("fa-sort-alpha-asc", "\uf15d");
		faMap.put("fa-sort-alpha-desc", "\uf15e");
		faMap.put("fa-sort-amount-asc", "\uf160");
		faMap.put("fa-sort-amount-desc", "\uf161");
		faMap.put("fa-sort-numeric-asc", "\uf162");
		faMap.put("fa-sort-numeric-desc", "\uf163");
		faMap.put("fa-thumbs-up", "\uf164");
		faMap.put("fa-thumbs-down", "\uf165");
		faMap.put("fa-youtube-square", "\uf166");
		faMap.put("fa-youtube", "\uf167");
		faMap.put("fa-xing", "\uf168");
		faMap.put("fa-xing-square", "\uf169");
		faMap.put("fa-youtube-play", "\uf16a");
		faMap.put("fa-dropbox", "\uf16b");
		faMap.put("fa-stack-overflow", "\uf16c");
		faMap.put("fa-instagram", "\uf16d");
		faMap.put("fa-flickr", "\uf16e");
		faMap.put("fa-adn", "\uf170");
		faMap.put("fa-bitbucket", "\uf171");
		faMap.put("fa-bitbucket-square", "\uf172");
		faMap.put("fa-tumblr", "\uf173");
		faMap.put("fa-tumblr-square", "\uf174");
		faMap.put("fa-long-arrow-down", "\uf175");
		faMap.put("fa-long-arrow-up", "\uf176");
		faMap.put("fa-long-arrow-left", "\uf177");
		faMap.put("fa-long-arrow-right", "\uf178");
		faMap.put("fa-apple", "\uf179");
		faMap.put("fa-windows", "\uf17a");
		faMap.put("fa-android", "\uf17b");
		faMap.put("fa-linux", "\uf17c");
		faMap.put("fa-dribbble", "\uf17d");
		faMap.put("fa-skype", "\uf17e");
		faMap.put("fa-foursquare", "\uf180");
		faMap.put("fa-trello", "\uf181");
		faMap.put("fa-female", "\uf182");
		faMap.put("fa-male", "\uf183");
		faMap.put("fa-gittip", "\uf184");
		faMap.put("fa-sun-o", "\uf185");
		faMap.put("fa-moon-o", "\uf186");
		faMap.put("fa-archive", "\uf187");
		faMap.put("fa-bug", "\uf188");
		faMap.put("fa-vk", "\uf189");
		faMap.put("fa-weibo", "\uf18a");
		faMap.put("fa-renren", "\uf18b");
		faMap.put("fa-pagelines", "\uf18c");
		faMap.put("fa-stack-exchange", "\uf18d");
		faMap.put("fa-arrow-circle-o-right", "\uf18e");
		faMap.put("fa-arrow-circle-o-left", "\uf190");
		faMap.put("fa-caret-square-o-left", "\uf191");
		faMap.put("fa-dot-circle-o", "\uf192");
		faMap.put("fa-wheelchair", "\uf193");
		faMap.put("fa-vimeo-square", "\uf194");
	}
		
	public static Map<String, String> getFaMap()
	{
		return faMap;
	}
	
}
