(function () { 

"use strict";

angular.module('config', [])

.constant('siteConfig', {title:'',supportEmail:'jbg@pvblic.co'})

.constant('ENV', {name:'development',baseURL:'localhost',apiEndpoint:'http://localhost:8080/listing-rest/'})

; 
}());