<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>Invoice</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="${grailsApplication.config.grails.serverURL}/css/style.css"
	media="all" />
<link rel="stylesheet" type="text/css"
	href="${grailsApplication.config.grails.serverURL}/css/print.css"
	media="all" />
</head>
<body>
	<h2 class="pagehead">
		${customer.currentFirstName}
		${customer.currentLastName}
	</h2>

	<div class="row">
		<div class="col-sm-6">
			${customer.currentFirstName}
			${customer.currentLastName}
		</div>

		<div class="col-sm-6">
			<div class="panel panel-info">
				<div class="panel-heading">
					Postal Address 
				</div>
				<ul class="list-group">
					<li class="list-group-item">
						Postal Address	
					</li>
					<li class="list-group-item">
						Postal Address	
					</li>
					<li class="list-group-item">
						Postal Address	
					</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-sm-6">
			${customer.currentFirstName}
			${customer.currentLastName}
		</div>
	</div>
</body>
</html>
