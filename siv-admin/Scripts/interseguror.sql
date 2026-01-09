select rate , monedainidesc , monedafindesc , fecha from interseguro.view_tipo_cambio_diario 
where monedainidesc ='Nuevo Sol' and monedafindesc ='US Dolar' and  to_char(fecha,'dd/mm/rrrr') ='28/02/2022';

select rate , monedainidesc , monedafindesc , fecha from interseguro.view_tipo_cambio_diario 
where monedainidesc ='US Dolar' and monedafindesc ='Nuevo Sol' and  to_char(fecha,'dd/mm/rrrr') ='14/01/2022';
