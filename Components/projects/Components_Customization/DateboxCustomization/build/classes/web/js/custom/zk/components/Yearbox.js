//
// Tested with ZK 6.0.2
//
(function () {
	// simply extends Datebox,
	// what we need is only the widget class
	// custom.zk.components.Yearbox
	custom.zk.components.Yearbox = zk.$extends(zul.db.Datebox, {
	});
})();