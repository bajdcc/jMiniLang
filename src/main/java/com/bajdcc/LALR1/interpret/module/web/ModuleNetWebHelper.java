package com.bajdcc.LALR1.interpret.module.web;

import java.util.HashMap;
import java.util.Map;

/**
 * 实用方法
 *
 * @author bajdcc
 */
public class ModuleNetWebHelper {

	public static String getStatusCodeText(int code) {
		switch (code) {
			case 100: return "Continue";
			case 101: return "Switching Protocols";
			case 102: return "Processing";

			case 200: return "OK";
			case 201: return "Created";
			case 202: return "Accepted";
			case 203: return "Non-Authoriative Information";
			case 204: return "No Content";
			case 205: return "Reset Content";
			case 206: return "Partial Content";
			case 207: return "Multi-Status";

			case 300: return "Multiple Choices";
			case 301: return "Moved Permanently";
			case 302: return "Found";
			case 303: return "See Other";
			case 304: return "Not Modified";
			case 305: return "Use Proxy";
			case 306: return "(Unused)";
			case 307: return "Temporary Redirect";

			case 400: return "Bad Request";
			case 401: return "Unauthorized";
			case 402: return "Payment Granted";
			case 403: return "Forbidden";
			case 404: return "Not Found";
			case 405: return "Method Not Allowed";
			case 406: return "Not Acceptable";
			case 407: return "Proxy Authentication Required";
			case 408: return "Request Time-out";
			case 409: return "Conflict";
			case 410: return "Gone";
			case 411: return "Length Required";
			case 412: return "Precondition Failed";
			case 413: return "Request Entity Too Large";
			case 414: return "Request-URI Too Large";
			case 415: return "Unsupported Media Type";
			case 416: return "Requested range not satisfiable";
			case 417: return "Expectation Failed";
			case 422: return "Unprocessable Entity";
			case 423: return "Locked";
			case 424: return "Failed Dependency";

			case 500: return "Internal Server Error";
			case 501: return "Not Implemented";
			case 502: return "Bad Gateway";
			case 503: return "Service Unavailable";
			case 504: return "Gateway Timeout";
			case 505: return "HTTP Version Not Supported";
			case 507: return "Insufficient Storage";
		}
		return "Unknown";
	}

	private static Map<String, String> mapMime;

	public static String getMimeByExtension(String ext) {
		if (mapMime == null) {
			mapMime = new HashMap<>();
			mapMime.put("323", "text/h323");
			mapMime.put("acx", "application/internet-property-stream");
			mapMime.put("ai", "application/postscript");
			mapMime.put("aif", "audio/x-aiff");
			mapMime.put("aifc", "audio/x-aiff");
			mapMime.put("aiff", "audio/x-aiff");
			mapMime.put("asf", "video/x-ms-asf");
			mapMime.put("asr", "video/x-ms-asf");
			mapMime.put("asx", "video/x-ms-asf");
			mapMime.put("au", "audio/basic");
			mapMime.put("avi", "video/x-msvideo");
			mapMime.put("axs", "application/olescript");
			mapMime.put("bas", "text/plain");
			mapMime.put("bcpio", "application/x-bcpio");
			mapMime.put("bin", "application/octet-stream");
			mapMime.put("bmp", "image/bmp");
			mapMime.put("c", "text/plain");
			mapMime.put("cat", "application/vnd.ms-pkiseccat");
			mapMime.put("cdf", "application/x-cdf");
			mapMime.put("cer", "application/x-x509-ca-cert");
			mapMime.put("class", "application/octet-stream");
			mapMime.put("clp", "application/x-msclip");
			mapMime.put("cmx", "image/x-cmx");
			mapMime.put("cod", "image/cis-cod");
			mapMime.put("cpio", "application/x-cpio");
			mapMime.put("crd", "application/x-mscardfile");
			mapMime.put("crl", "application/pkix-crl");
			mapMime.put("crt", "application/x-x509-ca-cert");
			mapMime.put("csh", "application/x-csh");
			mapMime.put("css", "text/css");
			mapMime.put("dcr", "application/x-director");
			mapMime.put("der", "application/x-x509-ca-cert");
			mapMime.put("dir", "application/x-director");
			mapMime.put("dll", "application/x-msdownload");
			mapMime.put("dms", "application/octet-stream");
			mapMime.put("doc", "application/msword");
			mapMime.put("dot", "application/msword");
			mapMime.put("dvi", "application/x-dvi");
			mapMime.put("dxr", "application/x-director");
			mapMime.put("eps", "application/postscript");
			mapMime.put("etx", "text/x-setext");
			mapMime.put("evy", "application/envoy");
			mapMime.put("exe", "application/octet-stream");
			mapMime.put("fif", "application/fractals");
			mapMime.put("flr", "x-world/x-vrml");
			mapMime.put("gif", "image/gif");
			mapMime.put("gtar", "application/x-gtar");
			mapMime.put("gz", "application/x-gzip");
			mapMime.put("h", "text/plain");
			mapMime.put("hdf", "application/x-hdf");
			mapMime.put("hlp", "application/winhlp");
			mapMime.put("hqx", "application/mac-binhex40");
			mapMime.put("hta", "application/hta");
			mapMime.put("htc", "text/x-component");
			mapMime.put("htm", "text/html");
			mapMime.put("html", "text/html");
			mapMime.put("html-utf8", "text/html;charset=utf-8");
			mapMime.put("htt", "text/webviewhtml");
			mapMime.put("ico", "image/x-icon");
			mapMime.put("ief", "image/ief");
			mapMime.put("iii", "application/x-iphone");
			mapMime.put("ins", "application/x-internet-signup");
			mapMime.put("isp", "application/x-internet-signup");
			mapMime.put("jfif", "image/pipeg");
			mapMime.put("jpe", "image/jpeg");
			mapMime.put("jpeg", "image/jpeg");
			mapMime.put("jpg", "image/jpeg");
			mapMime.put("js", "application/x-javascript");
			mapMime.put("latex", "application/x-latex");
			mapMime.put("lha", "application/octet-stream");
			mapMime.put("lsf", "video/x-la-asf");
			mapMime.put("lsx", "video/x-la-asf");
			mapMime.put("lzh", "application/octet-stream");
			mapMime.put("m13", "application/x-msmediaview");
			mapMime.put("m14", "application/x-msmediaview");
			mapMime.put("m3u", "audio/x-mpegurl");
			mapMime.put("man", "application/x-troff-man");
			mapMime.put("mdb", "application/x-msaccess");
			mapMime.put("me", "application/x-troff-me");
			mapMime.put("mht", "message/rfc822");
			mapMime.put("mhtml", "message/rfc822");
			mapMime.put("mid", "audio/mid");
			mapMime.put("mny", "application/x-msmoney");
			mapMime.put("mov", "video/quicktime");
			mapMime.put("movie", "video/x-sgi-movie");
			mapMime.put("mp2", "video/mpeg");
			mapMime.put("mp3", "audio/mpeg");
			mapMime.put("mpa", "video/mpeg");
			mapMime.put("mpe", "video/mpeg");
			mapMime.put("mpeg", "video/mpeg");
			mapMime.put("mpg", "video/mpeg");
			mapMime.put("mpp", "application/vnd.ms-project");
			mapMime.put("mpv2", "video/mpeg");
			mapMime.put("ms", "application/x-troff-ms");
			mapMime.put("mvb", "application/x-msmediaview");
			mapMime.put("nws", "message/rfc822");
			mapMime.put("oda", "application/oda");
			mapMime.put("p10", "application/pkcs10");
			mapMime.put("p12", "application/x-pkcs12");
			mapMime.put("p7b", "application/x-pkcs7-certificates");
			mapMime.put("p7c", "application/x-pkcs7-mime");
			mapMime.put("p7m", "application/x-pkcs7-mime");
			mapMime.put("p7r", "application/x-pkcs7-certreqresp");
			mapMime.put("p7s", "application/x-pkcs7-signature");
			mapMime.put("pbm", "image/x-portable-bitmap");
			mapMime.put("pdf", "application/pdf");
			mapMime.put("pfx", "application/x-pkcs12");
			mapMime.put("pgm", "image/x-portable-graymap");
			mapMime.put("pko", "application/ynd.ms-pkipko");
			mapMime.put("pma", "application/x-perfmon");
			mapMime.put("pmc", "application/x-perfmon");
			mapMime.put("pml", "application/x-perfmon");
			mapMime.put("pmr", "application/x-perfmon");
			mapMime.put("pmw", "application/x-perfmon");
			mapMime.put("pnm", "image/x-portable-anymap");
			mapMime.put("pot", "application/vnd.ms-powerpoint");
			mapMime.put("ppm", "image/x-portable-pixmap");
			mapMime.put("pps", "application/vnd.ms-powerpoint");
			mapMime.put("ppt", "application/vnd.ms-powerpoint");
			mapMime.put("prf", "application/pics-rules");
			mapMime.put("ps", "application/postscript");
			mapMime.put("pub", "application/x-mspublisher");
			mapMime.put("qt", "video/quicktime");
			mapMime.put("ra", "audio/x-pn-realaudio");
			mapMime.put("ram", "audio/x-pn-realaudio");
			mapMime.put("ras", "image/x-cmu-raster");
			mapMime.put("rgb", "image/x-rgb");
			mapMime.put("rmi", "audio/mid");
			mapMime.put("roff", "application/x-troff");
			mapMime.put("rtf", "application/rtf");
			mapMime.put("rtx", "text/richtext");
			mapMime.put("scd", "application/x-msschedule");
			mapMime.put("sct", "text/scriptlet");
			mapMime.put("setpay", "application/set-payment-initiation");
			mapMime.put("setreg", "application/set-registration-initiation");
			mapMime.put("sh", "application/x-sh");
			mapMime.put("shar", "application/x-shar");
			mapMime.put("sit", "application/x-stuffit");
			mapMime.put("snd", "audio/basic");
			mapMime.put("spc", "application/x-pkcs7-certificates");
			mapMime.put("spl", "application/futuresplash");
			mapMime.put("src", "application/x-wais-source");
			mapMime.put("sst", "application/vnd.ms-pkicertstore");
			mapMime.put("stl", "application/vnd.ms-pkistl");
			mapMime.put("stm", "text/html");
			mapMime.put("svg", "image/svg+xml");
			mapMime.put("sv4cpio", "application/x-sv4cpio");
			mapMime.put("sv4crc", "application/x-sv4crc");
			mapMime.put("swf", "application/x-shockwave-flash");
			mapMime.put("t", "application/x-troff");
			mapMime.put("tar", "application/x-tar");
			mapMime.put("tcl", "application/x-tcl");
			mapMime.put("tex", "application/x-tex");
			mapMime.put("texi", "application/x-texinfo");
			mapMime.put("texinfo", "application/x-texinfo");
			mapMime.put("tgz", "application/x-compressed");
			mapMime.put("tif", "image/tiff");
			mapMime.put("tiff", "image/tiff");
			mapMime.put("tr", "application/x-troff");
			mapMime.put("trm", "application/x-msterminal");
			mapMime.put("tsv", "text/tab-separated-values");
			mapMime.put("txt", "text/plain");
			mapMime.put("uls", "text/iuls");
			mapMime.put("ustar", "application/x-ustar");
			mapMime.put("vcf", "text/x-vcard");
			mapMime.put("vrml", "x-world/x-vrml");
			mapMime.put("wav", "audio/x-wav");
			mapMime.put("wcm", "application/vnd.ms-works");
			mapMime.put("wdb", "application/vnd.ms-works");
			mapMime.put("wks", "application/vnd.ms-works");
			mapMime.put("wmf", "application/x-msmetafile");
			mapMime.put("wps", "application/vnd.ms-works");
			mapMime.put("wri", "application/x-mswrite");
			mapMime.put("wrl", "x-world/x-vrml");
			mapMime.put("wrz", "x-world/x-vrml");
			mapMime.put("xaf", "x-world/x-vrml");
			mapMime.put("xbm", "image/x-xbitmap");
			mapMime.put("xla", "application/vnd.ms-excel");
			mapMime.put("xlc", "application/vnd.ms-excel");
			mapMime.put("xlm", "application/vnd.ms-excel");
			mapMime.put("xls", "application/vnd.ms-excel");
			mapMime.put("xlt", "application/vnd.ms-excel");
			mapMime.put("xlw", "application/vnd.ms-excel");
			mapMime.put("xof", "x-world/x-vrml");
			mapMime.put("xpm", "image/x-xpixmap");
			mapMime.put("xwd", "image/x-xwindowdump");
			mapMime.put("z", "application/x-compress");
			mapMime.put("zip", "application/zip");
		}
		if (ext == null || ext.isEmpty()) {
			return "application/octet-stream";
		}
		return mapMime.getOrDefault(ext, "application/octet-stream");
	}
}
