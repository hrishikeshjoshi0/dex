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
	<div class="row">
		<div class="col-lg-8 col-md-8">
			<div class="row">
				<div class="col-md-6">
					<div class="col-md-12">
						<div class="panel panel-default">
							<ul class="list-group">
								<li class="list-group-item">Invoice# : ${invoice.invoiceNumber}</li>
								<li class="list-group-item">Invoice date : ${invoice.invoiceDate}</li>
							</ul>
						</div>
					</div>
				</div>

				<div class="col-md-6">
					<div class="col-md-12 form-group">
						<ul class="list-group">
							<li class="list-group-item">
								${invoice.party.displayName}
							</li>
							<%--<li class="list-group-item">
								{{addressData.address1}} <br /> 
								{{addressData.address2}} <br />
								{{addressData.city}} <br /> 
								{{addressData.directions}}
							</li>
							--%>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
