<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<custom:layout title="My Blog">
    <h1>Articles List</h1>
    <h4>Articles count: ${fn:length(articles)}</h4>
    <form:form method="POST" action="${pageContext.request.contextPath}/select-articles"
        modelAttribute="selection" class="form-horizontal col-md-9 col-lg-6">
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>Number</th>
            <th>Title</th>
            <th>Content</th>
            <th>Date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="article" varStatus="status" items="${articles}">
            <tr>
                <td>
                    <form:checkbox path="articleIds" value="${article.id}"/>
                </td>
                <td>${status.index}</td>
                <td>${article.title}</td>
                <td>${article.content}</td>
                <td><fmt:formatDate pattern="dd.MM.yyyy" value="${article.createdDate}" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form:errors path="*" class="errors well bg-danger col-sm-12"/>

    <div class="col-sm-offset-1 col-sm-11">
        <button type="submit" class="btn btn-default">Select Articles</button>
        <input type="submit" name="clear" class="btn btn-danger" value="Clear">
        <a class="btn btn-primary" href="new-article">Add New Article</a>
    </div>
    </form:form>


    </div>
</custom:layout>