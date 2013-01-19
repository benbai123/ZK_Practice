//
// Tested with ZK 6.0.2
//
(function () {
	// simply extends Datebox,
	// what we need is only the widget class
	// custom.zk.components.Monthbox
	custom.zk.components.Monthbox = zk.$extends(zul.db.Datebox, {
	});
})();