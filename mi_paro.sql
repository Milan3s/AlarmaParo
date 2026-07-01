-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 01-07-2026 a las 11:02:57
-- Versión del servidor: 10.11.14-MariaDB-0ubuntu0.24.04.1
-- Versión de PHP: 8.2.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `mi_paro`
--

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `calendario`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `calendario` (
`evento_id` int(10) unsigned
,`usuario_id` int(10) unsigned
,`nombre` varchar(100)
,`fecha` date
,`tipo` varchar(12)
,`dias_antes` int(11)
,`descripcion` varchar(114)
);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_renovaciones`
--

CREATE TABLE `historial_renovaciones` (
  `id` int(10) UNSIGNED NOT NULL,
  `usuario_id` int(10) UNSIGNED NOT NULL,
  `fecha_renovacion` date NOT NULL,
  `fecha_siguiente` date NOT NULL,
  `observaciones` text DEFAULT NULL,
  `fecha_registro` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recordatorios`
--

CREATE TABLE `recordatorios` (
  `id` int(10) UNSIGNED NOT NULL,
  `renovacion_id` int(10) UNSIGNED NOT NULL,
  `dias_antes` int(11) NOT NULL,
  `enviado` tinyint(1) DEFAULT 0,
  `fecha_envio` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `renovaciones`
--

CREATE TABLE `renovaciones` (
  `id` int(10) UNSIGNED NOT NULL,
  `usuario_id` int(10) UNSIGNED NOT NULL,
  `fecha_renovacion` date NOT NULL,
  `fecha_siguiente` date NOT NULL,
  `estado` enum('pendiente','renovada','caducada') DEFAULT 'pendiente',
  `observaciones` text DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `dni` varchar(20) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `fecha_alta` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `historial_renovaciones`
--
ALTER TABLE `historial_renovaciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_historial_usuario` (`usuario_id`);

--
-- Indices de la tabla `recordatorios`
--
ALTER TABLE `recordatorios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_recordatorios_renovacion` (`renovacion_id`);

--
-- Indices de la tabla `renovaciones`
--
ALTER TABLE `renovaciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_renovaciones_usuario` (`usuario_id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `dni` (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `historial_renovaciones`
--
ALTER TABLE `historial_renovaciones`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `recordatorios`
--
ALTER TABLE `recordatorios`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `renovaciones`
--
ALTER TABLE `renovaciones`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

-- --------------------------------------------------------

--
-- Estructura para la vista `calendario`
--
DROP TABLE IF EXISTS `calendario`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `calendario`  AS SELECT `r`.`id` AS `evento_id`, `u`.`id` AS `usuario_id`, `u`.`nombre` AS `nombre`, `r`.`fecha_siguiente` AS `fecha`, 'RENOVACION' AS `tipo`, NULL AS `dias_antes`, concat('Renovación de ',`u`.`nombre`) AS `descripcion` FROM (`renovaciones` `r` join `usuarios` `u` on(`u`.`id` = `r`.`usuario_id`))union all select `rec`.`id` AS `evento_id`,`u`.`id` AS `usuario_id`,`u`.`nombre` AS `nombre`,`r`.`fecha_siguiente` - interval `rec`.`dias_antes` day AS `fecha`,'RECORDATORIO' AS `tipo`,`rec`.`dias_antes` AS `dias_antes`,concat('Recordatorio ',`rec`.`dias_antes`,' días antes') AS `descripcion` from ((`recordatorios` `rec` join `renovaciones` `r` on(`r`.`id` = `rec`.`renovacion_id`)) join `usuarios` `u` on(`u`.`id` = `r`.`usuario_id`))  ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `historial_renovaciones`
--
ALTER TABLE `historial_renovaciones`
  ADD CONSTRAINT `fk_historial_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `recordatorios`
--
ALTER TABLE `recordatorios`
  ADD CONSTRAINT `fk_recordatorios_renovacion` FOREIGN KEY (`renovacion_id`) REFERENCES `renovaciones` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `renovaciones`
--
ALTER TABLE `renovaciones`
  ADD CONSTRAINT `fk_renovaciones_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
