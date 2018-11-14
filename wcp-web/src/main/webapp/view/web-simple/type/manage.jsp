<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>检索-<PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="index,follow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<%--<jsp:include page="../commons/superContent.jsp"></jsp:include>--%>
	<div class="containerbox">
			<iframe scrolling="auto" frameborder="0" src="webtype/list.do"
					style="width: 100%; height: 100vh !important;"></iframe>
	</div>
	<%--<a id="scrollToTOP">
		<img src="<PF:basePath/>/text/img/top.png" title="返回顶部" id="gotop" alt="返回顶部" style="position: fixed;bottom: 78px;right: 50px;">
	</a>--%>

	<%--<jsp:include page="../commons/footServer.jsp"></jsp:include>--%>
	<%--<jsp:include page="../commons/foot.jsp"></jsp:include>--%>
</body>
</html>