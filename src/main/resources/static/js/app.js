var myApp = angular.module("myApp", []);

myApp.controller("rootController", ["$scope", "$http", function($scope) {
    $scope.openModal = function() {
        $('#myModal').modal('show');
    };
    $scope.closeModal = function() {
        $('#myModal').modal('hide');
    };

    $scope.carKdv = carKdv;
    $scope.homeKdv = homeKdv;
    $scope.healthKdv = healthKdv;
    
    // Watch fonksiyonu product type değiştiğinde updateKdvRate fonksiyonunu çağırır.
    $scope.$watch('productType', function(newVal) {
        console.log("4 $scope.productType:", newVal);
        $scope.updateKdvRate();
    });

    $scope.updateKdvRate = function() {
        if ($scope.productType == 1 ) {
            $scope.kdvRate = $scope.carKdv.kdvRate;
        } else if ($scope.productType == 2 ) {
            $scope.kdvRate = $scope.homeKdv.kdvRate;
        } else if ($scope.productType == 3 ) {
            $scope.kdvRate = $scope.healthKdv.kdvRate;
        }
    };   

}]);


myApp.controller("homeController", ["$scope", "$http", function($scope) {
    $scope.openModal = function() {
        $('#renewModal').modal('show');
    };
    $scope.closeModal = function() {
        $('#renewModal').modal('hide');
    };

    $scope.period = period;   

}]);


//myApp.controller("registerCustomerFormController", ["$scope", function($scope) {
//
//	$scope.register = function() {
//		$scope.msg = "Welcome " + $scope.firstname + "! You have signed in.";
//
//	};
//
//	var today = new Date();
//	today.setFullYear(today.getFullYear() - 18); // Bugünden 18 yıl önceki tarih
//
//	var birthInput = document.getElementById("birth");
//	birthInput.setAttribute("max", today.toISOString().split("T")[0]);
//
//}]);

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

}]);


myApp.controller("healthInsuranceFormController", ["$scope", function($scope) {

	$scope.register = function() {
		$scope.msg = "Welcome " + $scope.firstname + "! You have signed in.";

	};
	
	//Select the Insurance Period
	$scope.periods = [30, 60];
	$scope.period = "";
	
	//Select Job
	$scope.jobs = ["Askeri Personel", "Banka ve Finans Kurumu Çalışanı", "Çiftçi", "Diğer", "Din Adamları", "Eczacı", "Eğitim Sektörü Çalışanı", "Emekli", "Emniyet Mensubu", "Esnaf", "Ev Hanımı", "Kamu Çalışanı",  "Öğrenci", "Özel Sektör Çalışanı", "Sağlık Sektörü Çalışanı", "Uçuş Personeli", "Veteriner", "Yargı Mensubu"];
	$scope.job = "";

	//Select the For Who
	$scope.forWhose = ["Me", "My Family", "My Children"];
	$scope.forWho = "";

}]);

myApp.controller("registerFormController", ["$scope", function($scope){

    $scope.register = function(){
        $scope.msg = "This is a warning alert—check it out!";
    }
    
    var today = new Date();
	today.setFullYear(today.getFullYear() - 18); // Bugünden 18 yıl önceki tarih

	var birthInput = document.getElementById("birth");
	birthInput.setAttribute("max", today.toISOString().split("T")[0]);

}]);

function togglePasswordVisibility() {
    var password = document.getElementById("password");
    var hideIcon = document.getElementById("hideIcon");
    var showIcon = document.getElementById("showIcon");

    // Check the current type of the password input
    if (password.type === "password") {
        password.type = "text";
        showIcon.style.display = "none";
        hideIcon.style.display = "inline-block";
    } else {
        password.type = "password";
        showIcon.style.display = "inline-block";
        hideIcon.style.display = "none";
    }
}




