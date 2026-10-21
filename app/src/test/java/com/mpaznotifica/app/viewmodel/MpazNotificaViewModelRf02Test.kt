package com.mpaznotifica.app.viewmodel

import com.mpaznotifica.app.model.AlcanceEnvio
import com.mpaznotifica.app.model.Perfil
import com.mpaznotifica.app.model.TipoComunicacion
import com.mpaznotifica.app.model.Usuario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MpazNotificaViewModelRf02Test {
    private val viewModel = MpazNotificaViewModel()
    private val inspectoria = Usuario(
        id = "ins-test",
        nombre = "Inspectoria de prueba",
        usuario = "inspectoria_test",
        clave = "ficticia_solo_test",
        perfil = Perfil.INSPECTORIA_GENERAL
    )
    private val profesorJefe = Usuario(
        id = "prof-test",
        nombre = "Docente de prueba",
        usuario = "profesor_test",
        clave = "ficticia_solo_test",
        perfil = Perfil.PROFESOR_JEFE,
        cursoAsignado = "1A"
    )

    @Test
    fun inspectoriaRecibeTodosLosTipos() {
        assertEquals(TipoComunicacion.entries.toList(), viewModel.tiposPermitidos(inspectoria))
    }

    @Test
    fun inspectoriaRecibeTodosLosAlcances() {
        assertEquals(AlcanceEnvio.entries.toList(), viewModel.alcancesPermitidos(inspectoria))
    }

    @Test
    fun profesorJefeRecibeExclusivamenteLosTiposAutorizados() {
        val tipos = viewModel.tiposPermitidos(profesorJefe)

        assertEquals(2, tipos.size)
        assertEquals(setOf(TipoComunicacion.PEDAGOGICA, TipoComunicacion.CITACION), tipos.toSet())
    }

    @Test
    fun profesorJefeRecibeExclusivamenteLosAlcancesAutorizados() {
        val alcances = viewModel.alcancesPermitidos(profesorJefe)

        assertEquals(2, alcances.size)
        assertEquals(setOf(AlcanceEnvio.CURSO, AlcanceEnvio.APODERADO_INDIVIDUAL), alcances.toSet())
    }

    @Test
    fun usuarioNuloNoRecibeTiposNiAlcances() {
        assertTrue(viewModel.tiposPermitidos(null).isEmpty())
        assertTrue(viewModel.alcancesPermitidos(null).isEmpty())
    }

    @Test
    fun profesorJefeNoRecibeTiposProhibidos() {
        val tipos = viewModel.tiposPermitidos(profesorJefe)

        assertFalse(TipoComunicacion.INSTITUCIONAL in tipos)
        assertFalse(TipoComunicacion.DISCIPLINARIA in tipos)
        assertFalse(TipoComunicacion.EVENTO in tipos)
        assertFalse(TipoComunicacion.TALLER in tipos)
        assertFalse(TipoComunicacion.SALIDA_PEDAGOGICA in tipos)
    }

    @Test
    fun profesorJefeNoRecibeColegioCompletoNiTaller() {
        val alcances = viewModel.alcancesPermitidos(profesorJefe)

        assertFalse(AlcanceEnvio.COLEGIO_COMPLETO in alcances)
        assertFalse(AlcanceEnvio.TALLER in alcances)
    }
}
