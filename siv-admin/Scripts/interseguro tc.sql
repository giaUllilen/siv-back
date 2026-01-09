select rate , monedainidesc , monedafindesc , fecha 
from interseguro.view_tipo_cambio_diario 
where monedainidesc ='US Dolar' and monedafindesc ='Nuevo Sol'
and  to_char(fecha,'dd/mm/rrrr') ='28/02/2022';
	

 -- si prop 1 es sole y prop 2 es dolares la prop 1 se convertiria a dolares

select rate , monedainidesc , monedafindesc , fecha 
from interseguro.view_tipo_cambio_diario 
where monedainidesc ='US Dolar' and monedafindesc ='Nuevo Sol'
and  to_char(fecha,'dd/mm/rrrr') ='28/02/2022';

 -- si prop 1 es dolares y prop 2 es sole la prop 1 se convertiria a soles

select rate , monedainidesc , monedafindesc , fecha 
from interseguro.view_tipo_cambio_diario 
where monedainidesc ='Nuevo Sol' and monedafindesc ='US Dolar'
and  to_char(fecha,'dd/mm/rrrr') ='28/02/2022';