/*
 * ------------------------------------------------------------
 * Script cotiza-db
 * ------------------------------------------------------------
 */

use cotiza-db;


//-- -----------------------------------------------------------------------------------------------
//-- DROPs
//-- -----------------------------------------------------------------------------------------------

db.multitabla.drop();


//-- -----------------------------------------------------------------------------------------------
//-- CREATEs
//-- -----------------------------------------------------------------------------------------------

db.createCollection('multitabla', { storageEngine: {wiredTiger: { configString: 'block_compressor=none' }}});


//-- -----------------------------------------------------------------------------------------------
//-- INSERTs
//-- -----------------------------------------------------------------------------------------------


//-- multitabla
//-- -------------------------------------------------------

db.multitabla.insert([
	{
		codigo_tabla: "001",
		nombre_tabla: "Tipo de Documento",
		detalle_tabla: [
			{
				codigo: "1",
				orden: 1,
				valor: "DNI",
				valor_aux1: "",
				estado: "1"
			},
			{
				codigo: "2", 
				orden: 2,
				valor: "Carnet Extranjería",
				valor_aux1: "",
				estado: "1"
			}
		],
		estado_tabla: "1"
	},
	{
		codigo_tabla: "002",
		nombre_tabla: "Elección General",
		detalle_tabla: [
			{
				codigo: "1",
				orden: 1,
				valor: "Si",
				valor_aux1: "",
				estado: "1"
			},
			{
				codigo: "2", 
				orden: 2,
				valor: "No",
				valor_aux1: "",
				estado: "1"
			}
		],
		estado_tabla: "1"
	},
	{
		codigo_tabla: "003",
		nombre_tabla: "Estado General",
		detalle_tabla: [
			{
				codigo: "1",
				orden: 1,
				valor: "Activo",
				valor_aux1: "",
				estado: "1"
			},
			{
				codigo: "2", 
				orden: 2,
				valor: "Inactivo",
				valor_aux1: "",
				estado: "1"
			}
		],
		estado_tabla: "1"
	}
]);

