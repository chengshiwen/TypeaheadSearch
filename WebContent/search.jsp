<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="css/bootstrap.min.css" rel="stylesheet" />
	<title>地图搜索</title>
	<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript">
		function createXmlHttpRequest() {
			var oHttpReq = null;

			if (window.XMLHttpRequest) {
				oHttpReq = new XMLHttpRequest;
				if (oHttpReq.overrideMimeType) {
					oHttpReq.overrideMimeType("text/xml");
				}
			} else if (window.ActiveXObject) {
				try {
					oHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
				} catch (e) {
					oHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
				}
			} else if (window.createRequest) {
				oHttpReq = window.createRequest();
			} else {
				oHttpReq = new XMLHttpRequest();
			}

			return oHttpReq;
		}

		function doSearch(query) {
			query = encodeURIComponent(query, "utf-8");
			var url = "SearchServerServlet?query=" + query + "&lat=" + centerMarker.getPosition().lat() + "&lng=" + centerMarker.getPosition().lng();
			//1.创建XMLHttpRequest组建
			req = createXmlHttpRequest();
			//2.初始化XMLHttpRequest组建
			req.open("GET", url, true);
			//3.设置回调函数
			req.onreadystatechange = function() {
				if (req.readyState == 4) {
					if (req.status == 200) {
						// alert(req.responseText);
						var result = JSON.parse(req.responseText);	//获取返回的内容
						var output = '<ul>';
						for (var i = 0; i < result.length; i++) {
							output += '<li>';
							output += '<a style="color: #46b8da;" href="' + result[i]["url"] + '">' + result[i]["name"] + '</a>';
							output += '<p style="font-size: 12px;">' + result[i]["addr"].split("\n")[1] + '</p>';
							output += '</li>';
						}
						output += '</ul>';
						document.getElementById("showquery").innerHTML = output;

						for (var i = 0; i < Math.min(result.length, markerArray.length); i++) {
							var latlng = new google.maps.LatLng(result[i]["latlng"][0], result[i]["latlng"][1]);
							markerArray[i].setPosition(latlng);
						}

						if (result.length > markerArray.length) {
							for (var i = markerArray.length; i < result.length; i++) {
								var latlng = new google.maps.LatLng(result[i]["latlng"][0], result[i]["latlng"][1]);
								var marker = new google.maps.Marker({
									position: latlng,
									map: map,
									title: result[i]["name"],
									icon: "http://maps.google.com/mapfiles/marker_grey.png"
								});
								markerArray.push(marker);
							}
						} else {
							for (var i = result.length; i < markerArray.length; i++)
								markerArray[i].setMap(null);
							markerArray.splice(result.length, markerArray.length - result.length);
						}
					}
				}
			};
			//4.发送请求
			req.send(null);
		}

		function keypress() {
			var query = document.getElementById("querytext").value;
			if (query.length == 0) {
				return;
			}
			doSearch(query);
		}

		function setcenter1() {
			var text = prompt("Set Center: lat lng", centerMarker.getPosition().lat() + " " + centerMarker.getPosition().lng());
			var s = " ";
			if (text.indexOf(",") != -1)
				s = ",";
			centerMarker.setPosition(new google.maps.LatLng(parseFloat(text.split(s)[0]), parseFloat(text.split(s)[1])));
			map.setCenter(centerMarker.getPosition());
		}

		function initializeMap() {
			// var centerLatlng = new google.maps.LatLng(39.9998, 116.3264);
			var centerLatlng = new google.maps.LatLng(1.3819393338187156, 103.82039276123044);
			var myOptions = {
				zoom: 12,
				center: centerLatlng,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			}
			map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
			markerArray = new Array();
			centerMarker = new google.maps.Marker({
				position: centerLatlng,
				map: map,
				title: "Center Point",
				icon: "http://maps.google.com/mapfiles/marker_green.png"
			});
			centerMarker.setDraggable(true);
			google.maps.event.addListener(centerMarker, 'dragend', function() {
				keypress();
			});
			google.maps.event.addListener(centerMarker, 'click', function() {
			 	map.setCenter(centerMarker.getPosition());
			});
			google.maps.event.addListener(centerMarker, 'rightclick', function() {
				setcenter1();
			});
		}
	</script>
</head>
<body onload="initializeMap();">
	<nav class="navbar navbar-default" role="navigation">
		<div class="navbar-header">
			<img src="image/header.png" class="img-rounded" style= "margin-left: 30px; margin-right: 10px;">
		</div>
		<div class="collapse navbar-collapse">
			<div class="navbar-form navbar-left form-group" role="search">
				<input type="text" id="querytext" class="form-control" placeholder="Search" onkeyup="keypress();" />
			</div>
		</div>
	</nav>
	<div class="row" style="width: 1364px;">
		<div class="col-md-3">
			<span id="showquery"></span>
		</div>
		<div class="col-md-9">
			<div id="map_canvas" style="width: 100%; height: 555px;"></div>
		</div>
	</div>
</body>
</html>
