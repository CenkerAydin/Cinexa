package com.cenkeraydin.composemovie.ui.person

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.components.ModernSearchBar
import com.cenkeraydin.composemovie.components.person.PersonCard
import com.cenkeraydin.composemovie.components.person.PersonFilterDropdown
import com.cenkeraydin.composemovie.data.model.person.Person
import com.cenkeraydin.composemovie.util.UIState

@Composable
fun PersonScreen(
    navHostController: NavHostController,
    personViewModel: PersonViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    val personState by personViewModel.persons.collectAsState()
    val selectedFilter by personViewModel.personFilter.collectAsState()
    val searchQuery by personViewModel.searchQuery.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        personViewModel.fetchPerson()
    }

    Column(modifier = Modifier.padding(top = if (isLandscape) 8.dp else 32.dp)) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search
                ModernSearchBar(
                    query = searchQuery,
                    placeholder = stringResource(R.string.search_persons),
                    onQueryChange = { newQuery ->
                        personViewModel.startNewSearch(newQuery)
                    },
                    onSearch = {
                        personViewModel.searchPersons(it)
                    },
                    modifier = Modifier.weight(1f)

                )

                PersonFilterDropdown(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { personViewModel.setPersonFilter(it) }
                )

            }


        } else {

            ModernSearchBar(
                query = searchQuery,
                placeholder = stringResource(R.string.search_persons),
                onQueryChange = { newQuery ->
                    personViewModel.startNewSearch(newQuery)
                },
                onSearch = {
                    personViewModel.searchPersons(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PersonFilterDropdown(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { personViewModel.setPersonFilter(it) }
                )


            }
        }
        when (personState) {
            is UIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            is UIState.Error -> {
                val error = (personState as UIState.Error).message
                val errorMessage = when {
                    error.contains("Unable to resolve host", ignoreCase = true) -> stringResource(R.string.check_internet)
                    else -> stringResource(R.string.no_person)
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }


            is UIState.Success -> {
                val listState = rememberLazyListState()

                val persons = (personState as UIState.Success<List<Person>>).data
                LazyColumn(
                    state = listState
                ) {
                    itemsIndexed(persons) { index, person ->
                        val isFavorite by personViewModel.isFavorite(person.id)
                            .collectAsState(initial = false)
                        PersonCard(
                            name = person.name ?: stringResource(R.string.unknown),
                            knownForDepartment = person.knownForDepartment ?: "Bilinmeyen",
                            popularity = person.popularity,
                            imageUrl = person.profilePath ?: "",
                            isFavorite = isFavorite,
                            onFavoriteClick = {
                                personViewModel.toggleFavorite(person)
                            },
                            onClick = {
                                navHostController.navigate("person_detail_screen/${person.id}")
                            }
                        )
                        if (index >= persons.lastIndex - 3) {
                            personViewModel.loadMoreIfNeeded()
                        }
                    }
                }
            }

        }

    }
}