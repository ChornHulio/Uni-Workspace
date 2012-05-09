euklid <-
function(a,b) {
	.C("cEuklid",as.integer(a),as.integer(b),as.integer(ncol(a)), erg = integer)$erg
}

