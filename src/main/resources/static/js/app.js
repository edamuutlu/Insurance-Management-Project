var myApp = angular.module("myApp", []);

myApp.controller("formController", ["$scope", function($scope) {

	$scope.register = function() {
		$scope.msg = "Welcome " + $scope.firstname + "! You have signed in.";
		
		/*Concat Plate and Write to Mysql */
		// Kullanıcıdan alınan verileri $scope üzerinden alın
		var provinceNumber = $scope.provinceNumber;
		var charPlate = $scope.charPlate;
		var intPlate = $scope.intPlate;

		// Verileri birleştirin veya işleyin
		var plate = provinceNumber + charPlate + intPlate;

		// User nesnesine plate bilgisini atayın
		$scope.user.setPlate(plate);

		
	};

	//	$scope.bdControl = function() {
	//		var present = new Date().getUTCDate();
	//		var entered = new Date($scope.birth.value);
	//		
	//		if (present < entered) {
	//			console.log("Incorrect Login");
	//			$scope.msg = "Incorrect Login";
	//		} 
	//	};	

	/* Select Province and District */
	var provinceDistrictinfo = {
		Ankara: {
			cities: ["Akyurt", "Altındağ", "Ayaş", "Bala", "Beypazarı", "Çamlıdere", "Çankaya", "Çubuk", "Elmadağ", "Eti̇mesgut", "Evren", "Gölbaşı", "Güdül", "Haymana", "Kahramankazan", "Kaleci̇k", "Keçi̇ören", "Kizilcahamam", "Mamak", "Nallihan", "Polatlı", "Pursaklar", "Si̇ncan", "Şerefli̇koçhi̇sar", "Yeni̇mahalle"],
		},
		Bursa: {
			cities: ["Osmangazi", "Nilüfer", "Yıldırım", "Büyükorhan", "Gemlik", "Gürsu", "Harmancık", "İnegöl", "İznik", "Karacabey", "Keles", "Kestel", "Mudanya", "Mustafakemalpaşa", "Orhaneli", "Orhangazi", "Yenişehir"],
		},
		İstanbul: {
			cities: ["Adalar", "Arnavutköy", "Ataşehir", "Avcılar", "Bağcılar", "Bahçelievler", "Bakırköy", "Başakşehir", "Bayrampaşa", "Beşiktaş", "Beykoz", "Beylikdüzü", "Beyoğlu", "Büyükçekmece", "Çatalca", "Çekmeköy", "Esenler", "Esenyurt", "Eyüpsultan", "Fatih", "Gaziosmanpaşa", "Güngören", "Kadıköy", "Kağıthane", "Kartal", "Küçükçekmece", "Maltepe", "Pendik", "Sancaktepe", "Sarıyer", "Silivri", "Sultanbeyli", "Sultangazi", "Şile", "Şişli", "Tuzla", "Ümraniye", "Üsküdar", "Zeytinburnu"],
		},
	};

	window.onload = function() {
		const selectProvince = document.getElementById('province');
		const selectDistrict = document.getElementById('district');
		const selects = document.querySelectorAll('select');

		selectDistrict.disabled = true;

		selects.forEach(select => {
			select.style.cursor = select.disabled ? "auto" : "pointer";
		});

		for (let province in provinceDistrictinfo) {
			selectProvince.options[selectProvince.options.length] = new Option(province, province);
		}

		selectProvince.onchange = function(e) {
			selectDistrict.disabled = false;

			selects.forEach(select => {
				select.style.cursor = select.disabled ? "auto" : "pointer";
			});

			selectDistrict.length = 1;

			for (let district of provinceDistrictinfo[e.target.value].cities) {
				selectDistrict.options[selectDistrict.options.length] = new Option(district, district);
			}
		};
	};

	/*Select Brand */
	var brands = ["Audi", "BMW", "Citroen", "Dacia", "Fiat", "Hyundai", "Jeep", "Kia", "Lamborghini", "Mercedes", "Nissan", "Opel", "Renault", "Skoda", "Volvo"]; // Markaların listesi

	var selectBrand = document.getElementById("brand"); // Select elementini seçiyoruz

	// Markaları dropdown listesine ekleme
	for (var i = 0; i < brands.length; i++) {
		var option = document.createElement("option");
		option.value = brands[i];
		option.text = brands[i];
		selectBrand.appendChild(option);
	}

}]);