var myApp = angular.module("myApp", []);

myApp.controller("registerCustomerFormController", ["$scope", function($scope) {

	$scope.register = function() {
		$scope.msg = "Welcome " + $scope.firstname + "! You have signed in.";

	};

	/* Birth Date Control */
	//	var today = new Date().toISOString().split("T")[0];
	//	document.getElementById("birth").setAttribute("max", today);

	var today = new Date();
	today.setFullYear(today.getFullYear() - 18); // Bugünden 18 yıl önceki tarih

	var birthInput = document.getElementById("birth");
	birthInput.setAttribute("max", today.toISOString().split("T")[0]);


	/* Select Province and District */
//	var provinceDistrictinfo = {
//		Ankara: {
//			cities: ["Akyurt", "Altındağ", "Ayaş", "Bala", "Beypazarı", "Çamlıdere", "Çankaya", "Çubuk", "Elmadağ", "Eti̇mesgut", "Evren", "Gölbaşı", "Güdül", "Haymana", "Kahramankazan", "Kaleci̇k", "Keçi̇ören", "Kizilcahamam", "Mamak", "Nallihan", "Polatlı", "Pursaklar", "Si̇ncan", "Şerefli̇koçhi̇sar", "Yeni̇mahalle"],
//		},
//		Bursa: {
//			cities: ["Osmangazi", "Nilüfer", "Yıldırım", "Büyükorhan", "Gemlik", "Gürsu", "Harmancık", "İnegöl", "İznik", "Karacabey", "Keles", "Kestel", "Mudanya", "Mustafakemalpaşa", "Orhaneli", "Orhangazi", "Yenişehir"],
//		},
//		İstanbul: {
//			cities: ["Adalar", "Arnavutköy", "Ataşehir", "Avcılar", "Bağcılar", "Bahçelievler", "Bakırköy", "Başakşehir", "Bayrampaşa", "Beşiktaş", "Beykoz", "Beylikdüzü", "Beyoğlu", "Büyükçekmece", "Çatalca", "Çekmeköy", "Esenler", "Esenyurt", "Eyüpsultan", "Fatih", "Gaziosmanpaşa", "Güngören", "Kadıköy", "Kağıthane", "Kartal", "Küçükçekmece", "Maltepe", "Pendik", "Sancaktepe", "Sarıyer", "Silivri", "Sultanbeyli", "Sultangazi", "Şile", "Şişli", "Tuzla", "Ümraniye", "Üsküdar", "Zeytinburnu"],
//		},
//	};
//
//
//	window.onload = function() {
//		const selectProvince = document.getElementById('province');
//		const selectDistrict = document.getElementById('district');
//		const selects = document.querySelectorAll('select');
//
//		selectDistrict.disabled = true;
//
//		selects.forEach(select => {
//			select.style.cursor = select.disabled ? "auto" : "pointer";
//		});
//
//		for (let province in provinceDistrictinfo) {
//			selectProvince.options[selectProvince.options.length] = new Option(province, province);
//		}
//
//		selectProvince.onchange = function(e) {
//			selectDistrict.disabled = false;
//
//			selects.forEach(select => {
//				select.style.cursor = select.disabled ? "auto" : "pointer";
//			});
//
//			selectDistrict.length = 1;
//
//			for (let district of provinceDistrictinfo[e.target.value].cities) {
//				selectDistrict.options[selectDistrict.options.length] = new Option(district, district);
//			}
//		};
//	};

	

}]);

myApp.controller("trafficInsuranceFormController", ["$scope", function($scope) {

	$scope.register = function() {
		$scope.msg = "Welcome " + $scope.firstname + "! You have signed in.";

	};

	//Select Usage Type
	$scope.types = ["Car", "Truck", "Van"];
	$scope.type = "";

	//Select Purpose of Usage
	$scope.purposes = ["Private", "Commercial"];
	$scope.purpose = "";

	//Select Brand 
	$scope.brands = ["Audi", "BMW", "Citroen", "Dacia", "Fiat", "Hyundai", "Jeep", "Kia", "Lamborghini", "Mercedes", "Nissan", "Opel", "Renault", "Skoda", "Volvo"];
	$scope.brand = "";

	//Select Fuel Type
	$scope.fuelTypes = ["Petrol", "Diesel", "LPG", "Electric"];
	$scope.fuelType = "";

	//Select the Insurance Period
	$scope.periods = [30, 60];
	$scope.period = "";


}]);

myApp.controller("homeInsuranceFormController", ["$scope", function($scope) {

	$scope.register = function() {
		$scope.msg = "Welcome " + $scope.firstname + "! You have signed in.";

	};

	//Select the Building Type
	$scope.buildingTypes = ["Reinforced Concrete", "Other Structures"];
	$scope.buildingType = "";

	//Select the Building Type
	$scope.typesOfUse = ["Summer House", "Permanent Residence"];
	$scope.typeOfUse = "";

	//Select the Floor
	$scope.floors = ["Basement", "Ground floor", "1-10 Floor", "11-20 Floor", "20 or More Floors"];
	$scope.floor = "";

	//Select the Owner Title
	$scope.titles = ["Owner", "Tenant"];
	$scope.title = "";

	//Select the Insurance Period
	$scope.periods = [30, 60];
	$scope.period = "";

	//Select the Building Age
	$scope.years = [];
	for (let year = 2023; year >= 1975; year--) { // 1975'ten 2023'e kadar olan sayıları ekleyen döngü
		$scope.years.push(year);
	}
	$scope.year = "";

	/* Select Province and District */
//	var provinceDistrictinfo = {
//		Ankara: {
//			cities: ["Akyurt", "Altındağ", "Ayaş", "Bala", "Beypazarı", "Çamlıdere", "Çankaya", "Çubuk", "Elmadağ", "Eti̇mesgut", "Evren", "Gölbaşı", "Güdül", "Haymana", "Kahramankazan", "Kaleci̇k", "Keçi̇ören", "Kizilcahamam", "Mamak", "Nallihan", "Polatlı", "Pursaklar", "Si̇ncan", "Şerefli̇koçhi̇sar", "Yeni̇mahalle"],
//		},
//		Bursa: {
//			cities: ["Osmangazi", "Nilüfer", "Yıldırım", "Büyükorhan", "Gemlik", "Gürsu", "Harmancık", "İnegöl", "İznik", "Karacabey", "Keles", "Kestel", "Mudanya", "Mustafakemalpaşa", "Orhaneli", "Orhangazi", "Yenişehir"],
//		},
//		İstanbul: {
//			cities: ["Adalar", "Arnavutköy", "Ataşehir", "Avcılar", "Bağcılar", "Bahçelievler", "Bakırköy", "Başakşehir", "Bayrampaşa", "Beşiktaş", "Beykoz", "Beylikdüzü", "Beyoğlu", "Büyükçekmece", "Çatalca", "Çekmeköy", "Esenler", "Esenyurt", "Eyüpsultan", "Fatih", "Gaziosmanpaşa", "Güngören", "Kadıköy", "Kağıthane", "Kartal", "Küçükçekmece", "Maltepe", "Pendik", "Sancaktepe", "Sarıyer", "Silivri", "Sultanbeyli", "Sultangazi", "Şile", "Şişli", "Tuzla", "Ümraniye", "Üsküdar", "Zeytinburnu"],
//		},
//	};
//
//
//	window.onload = function() {
//		const selectProvince = document.getElementById('province');
//		const selectDistrict = document.getElementById('district');
//		const selects = document.querySelectorAll('select');
//
//		selectDistrict.disabled = true;
//
//		selects.forEach(select => {
//			select.style.cursor = select.disabled ? "auto" : "pointer";
//		});
//
//		for (let province in provinceDistrictinfo) {
//			selectProvince.options[selectProvince.options.length] = new Option(province, province);
//		}
//
//		selectProvince.onchange = function(e) {
//			selectDistrict.disabled = false;
//
//			selects.forEach(select => {
//				select.style.cursor = select.disabled ? "auto" : "pointer";
//			});
//
//			selectDistrict.length = 1;
//
//			for (let district of provinceDistrictinfo[e.target.value].cities) {
//				selectDistrict.options[selectDistrict.options.length] = new Option(district, district);
//			}
//		};
//	};

}]);

