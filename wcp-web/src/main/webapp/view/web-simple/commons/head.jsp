<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF" %>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC" %>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation"
     style="margin: 0px;">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target="#bs-example-navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
            <span class="icon-bar"></span> <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand"
           style="color: #ffffff; font-weight: bold; padding: 5px;"
           href="<DOC:defaultIndexPage/>"> <img
                src="<PF:basePath/>/text/img/logo.png" height="40" alt="WCP"
                title="WCP" align="middle"/>
        </a>
    </div>
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
            <PF:IfParameterEquals key="config.sys.opensource" val="false">
                <li class="active"><a href="home/Pubindex.html"><span
                        class="glyphicon glyphicon-home"></span>&nbsp;资讯</a></li>
            </PF:IfParameterEquals>
            <li class="active"><a href="<DOC:defaultIndexPage/>"><span class="glyphicon glyphicon-home"></span>&nbsp;主页</a></li>
            <li class="active"><a href="javascript:;" onclick="toType()"><span
                    class="glyphicon glyphicon-th"></span>&nbsp;分类</a></li>
            <%--<li class="active"><a href="webgroup/PubHome.html"><span
                    class="glyphicon glyphicon-tree-conifer"></span>&nbsp;小组</a></li>
            <li class="active"><a href="webstat/PubHome.html"><span
                    class="glyphicon glyphicon-stats"></span>&nbsp;荣誉</a></li>--%>
            <%--<li class="active"><a href="websearch/PubHome.html"><span
                    class="glyphicon glyphicon-search"></span>&nbsp;检索</a></li>--%>
            <PF:IfParameterEquals key="config.about" val="true">
                <li class="active"><a href="home/PubAbout.html"><span
                        class="glyphicon glyphicon-phone-alt"></span>&nbsp;联系方式</a></li>
            </PF:IfParameterEquals>
        </ul>
        <form class="navbar-form navbar-left hidden-xs hidden-sm" role="search">
            <%-- <div class="form-group">
                 <input type="text" name="word" id="wordId" value="${word}"
                        class="form-control input-sm" placeholder="查询公开资源"> <input
                     type="hidden" id="pageNumId" name="pagenum">
             </div>
             <button type="submit" class="btn btn-default btn-sm">检索</button>--%>
            <c:if test="${USEROBJ!=null}">
                <jsp:include page="../operation/includeCreatOperate.jsp"></jsp:include>
            </c:if>
        </form>

        <ul class="nav navbar-nav navbar-right" style="margin-right: 10px;">
            <c:if test="${USEROBJ==null}">
                <li class="active"><a href="login/webPage.html"><span
                        class="glyphicon glyphicon glyphicon-user"></span>&nbsp;登录</a></li>
            </c:if>
            <c:if test="${USEROBJ!=null}">
                <li class="active">
                    <a <%--href="webuser/PubHome.do"--%> href="javascript:void(0)">
                        <span class="glyphicon glyphicon-user"></span>
                        &nbsp;${USEROBJ.name}
                    </a>
                </li>
                <li class="active"><a href="login/webout.html"><span
                        class="glyphicon glyphicon-off"></span>&nbsp;注销</a></li>
            </c:if>
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</div>
<script type="text/javascript">

    function toFile(docId, fileid) {
        if(docId == ""){
            localStorage.removeItem("docId");
        }else {
            localStorage.setItem("docId", docId);
        }
        window.location = basePath + "webdoc/view/PubFile" + fileid + ".html";
    }

    function returnToDocFile() {
        var docId = localStorage.getItem("docId");
        if (docId) {
            window.location = basePath + "webdoc/view/Pub" + docId + ".html";
        }else{
            returnToType();
        }
    }

    function returnToType() {
        var typeId = localStorage.getItem("typeId");
        var fromWebPage = localStorage.getItem("fromWebPage");
        if(fromWebPage){
            localStorage.removeItem("fromWebPage");
            window.location = basePath + "/know/webdown.do?typeid=" + typeId;
            return ;
        }
        if (typeId) {
            window.location = basePath + "webtype/view" + typeId + "/Pub1.html";
        } else {
            window.location = basePath + "webtype/view/Pub1.html";
        }

    }

    function toType() {
        localStorage.removeItem("typeId");
        window.location = basePath + "webtype/view/Pub1.html";
    }

    function luceneSearch(key) {
        $('#wordId').val(key);
        /*$('#lucenesearchFormId').submit();*/
        $('#websearchpubdoId').submit();
    }

    function luceneSearchGo(page) {
        $('#pageNumId').val(page);
        /*$('#lucenesearchFormId').submit();*/
        $('#websearchpubdoId').submit();
    }

    function docTypeManage() {
        window.location = "webtype/manage.do";
    }

    $(function () {
        // $(window).scroll(function(){
        //     if($(window).scrollTop() > 100){
        //         $("#scrollToTOP").fadeIn(1000);//一秒渐入动画
        //     }else{
        //         $("#scrollToTOP").fadeOut(1000);//一秒渐隐动画
        //     }
        // });
        $("#scrollToTOP").click(function () {
            $('html,body').animate({scrollTop: 0}, 800);
        });
    });

    //-->
</script>