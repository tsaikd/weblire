<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>weblire</title>
		<style type="text/css">
th {
	text-align: right;
}
		</style>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<form action="features" method="post" enctype="multipart/form-data">
			<table>
				<tbody>
					<tr>
						<td colspan="2">POST /features in multipart/form-data</td>
					</tr>
					<tr>
						<th><span>image:</span></th>
						<td><input type="file" name="image"/></td>
					</tr>
					<tr>
						<th><span>sha512:</span></th>
						<td><input type="checkbox" name="sha512" checked="checked"/></td>
					</tr>
					<tr>
						<th><span>CEED:</span></th>
						<td><input type="checkbox" name="CEED" checked="checked"/></td>
					</tr>
					<tr>
						<th><span>ColorLayoutext:</span></th>
						<td><input type="checkbox" name="ColorLayoutext" checked="checked"/></td>
					</tr>
					<tr>
						<td colspan="2"><input type="submit"/></td>
					</tr>
				</tbody>
			</table>
		</form>
	</body>
</html>