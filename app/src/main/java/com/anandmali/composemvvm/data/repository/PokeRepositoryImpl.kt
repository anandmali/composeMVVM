package com.anandmali.composemvvm.data.repository

import com.anandmali.composemvvm.data.source.network.PokeApi
import com.anandmali.composemvvm.data.source.network.response.Pokemon
import com.anandmali.composemvvm.data.source.network.PokemonViewDTO
import com.anandmali.composemvvm.data.source.network.response.PokeDetailsResponse
import com.anandmali.composemvvm.data.source.network.toViewData
import com.anandmali.composemvvm.data.Resource
import javax.inject.Inject

private const val LIMIT = 100
private const val OFFSET = 0

class PokeRepositoryImpl @Inject constructor(private val pokeApi: PokeApi) : PokeRepository {

    override suspend fun getPokeList(): List<PokemonViewDTO> {
        val result = getPokemonList()
        return mapPokemonList(result)
    }

    override suspend fun getPokemonInfo(pokemonName: String): Resource<PokeDetailsResponse> {
        val response = try {
            pokeApi.getPokemonInfo(pokemonName)
        } catch(e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    private suspend fun getPokemonList(): List<Pokemon> {
        val response = pokeApi.getPokeList(LIMIT, OFFSET)
        if (response.isSuccessful) {
            response.body()?.let { body -> return body.results } ?: run { return emptyList() }
        }
        return emptyList()
    }

    private fun mapPokemonList(result: List<Pokemon>): List<PokemonViewDTO> {
        return result.map { it.toViewData() }.sortedBy { it.id }
    }
}