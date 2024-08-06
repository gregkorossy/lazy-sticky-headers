/*
 * Copyright 2024 Gergely Kőrössy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.gingerninja.lazy.sample.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.gingerninja.lazy.StickyHeaders

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lazy Sticky Headers")
                        Text("Contacts", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) {
        ContactList(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }
}

@Composable
private fun ContactList(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            items(
                items = names,
                key = { it },
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp,
                        )
                        .padding(start = 50.dp)
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                    ) {
                        Text(text = it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        StickyHeaders(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxHeight(),
            state = listState,
            key = { item ->
                names[item.index].first()
            },
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(50.dp)
                    // .padding(vertical = 20.dp)
                    // .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "${it.key}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }

        /*LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            state = listState
        ) {
            verticalListItems(startDate, settings)
        }*/
    }
}

private val names = listOf(
    "Aaryan Blake",
    "Abby Blackburn",
    "Adil Stanley",
    "Aimee Salazar",
    "Aishah Escobar",
    "Aiza Bonner",
    "Alexa Mcmahon",
    "Alison Franklin",
    "Alissa Yates",
    "Amanda Potts",
    "Ameer Zimmerman",
    "Andre Russell",
    "Angel Mcdaniel",
    "Antonia Frederick",
    "Antony Villarreal",
    "Ariana Joyce",
    "Athena Fields",
    "Austin Gutierrez",
    "Autumn Mclaughlin",
    "Ayla Mejia",
    "Aysha Blevins",
    "Bailey Park",
    "Bartosz Lambert",
    "Beatrix Huffman",
    "Beth Meyers",
    "Bethany Guerra",
    "Betsy Jensen",
    "Brooke Rivers",
    "Brooklyn Gilbert",
    "Bruce Downs",
    "Callie Pierce",
    "Candice Petty",
    "Carl Stevenson",
    "Carol Byrd",
    "Caroline Whitney",
    "Catherine Reyes",
    "Chad Lozano",
    "Christian Yoder",
    "Clementine Mccann",
    "Connor Clarke",
    "Connor Estrada",
    "Daisy Mayer",
    "Dalton Shaw",
    "Daniel Osborne",
    "Daniela Graham",
    "Daniela Mata",
    "Darcey Gregory",
    "Darren Nixon",
    "Deborah Frye",
    "Delores Casey",
    "Demi-Leigh Vega",
    "Eddie Baxter",
    "Edmund Ware",
    "Eesa Simpson",
    "Elena Mcgowan",
    "Eleni Howe",
    "Eliot Cortez",
    "Eliza Pugh",
    "Elsa Gates",
    "Elspeth Moss",
    "Emre Parsons",
    "Esha Acosta",
    "Evie Porter",
    "Fabian Wallace",
    "Fahad Reese",
    "Faiza Hubbard",
    "Farhan Proctor",
    "Farhan Rodgers",
    "Fatimah Doherty",
    "Felix Myers",
    "Findlay Bowen",
    "Fletcher Robles",
    "Floyd Ortega",
    "Franciszek Garza",
    "Fred Ali",
    "Freyja Holden",
    "Garfield Torres",
    "Gavin Jenkins",
    "Gerald Crane",
    "Haaris Austin",
    "Habiba Lloyd",
    "Hafsa Gilmore",
    "Hari O'Reilly",
    "Hasan Gonzalez",
    "Hashim Bullock",
    "Hayden Obrien",
    "Hayley Olsen",
    "Henrietta Hurst",
    "Hollie Mercado",
    "Hussain Blankenship",
    "Ida Sandoval",
    "India Mcdonald",
    "Ines Rasmussen",
    "Ishaan Perkins",
    "Jackson Mccann",
    "Jade Blackwell",
    "Jan Hunter",
    "Jan Walters",
    "Jared Malone",
    "Jaya Gallegos",
    "Jennie Lang",
    "Joan Price",
    "Joanna Morrow",
    "Johnathan Ferguson",
    "Johnny Gallagher",
    "Jonathan Knowles",
    "Jude Cameron",
    "Judy Espinoza",
    "Karol Horne",
    "Katie Nixon",
    "Keiran Cordova",
    "Kenneth Lamb",
    "Keyaan Holt",
    "Kieran Velez",
    "Kimberly Hickman",
    "Kirsten Connor",
    "Kitty Everett",
    "Kobi Finley",
    "Kye Norton",
    "Lena Nielsen",
    "Leslie Stanley",
    "Lorcan David",
    "Lydia Lynch",
    "Lyra David",
    "Mahdi Lynch",
    "Marco Sullivan",
    "Maria Landry",
    "Mariya Brady",
    "Markus Herman",
    "Markus Watts",
    "Martin Macias",
    "Mason Horne",
    "Matilda Bowman",
    "Maximilian Buchanan",
    "May Mcintyre",
    "Miah Winters",
    "Micheal Stephens",
    "Mohamad Mckay",
    "Mohamed Le",
    "Muhammed Escobar",
    "Murray Ashley",
    "Nadine Webster",
    "Nate Combs",
    "Nathanael Holder",
    "Nelson Welch",
    "Nieve Mahoney",
    "Nikolas Contreras",
    "Noel Strickland",
    "Norman Cruz",
    "Omari Richard",
    "Oscar Koch",
    "Patricia Simon",
    "Peter Cain",
    "Rachel Hendrix",
    "Rafael Kemp",
    "Rahim Walsh",
    "Raihan Gray",
    "Raymond Winters",
    "Riley Beasley",
    "Rosalie Mathis",
    "Rosanna Barry",
    "Russell Velazquez",
    "Sadia Duffy",
    "Sadie Beard",
    "Safiya Gibbs",
    "Safiyyah Franco",
    "Saif Mayer",
    "Samir Lawson",
    "Samuel Crosby",
    "Sana Clements",
    "Scott Davenport",
    "Serena Noble",
    "Shreya Gill",
    "Sonny Marshall",
    "Sulaiman Bates",
    "Sulayman Cain",
    "Summer Hart",
    "Tanya Sims",
    "Tariq Luna",
    "Terry Bullock",
    "Tessa Diaz",
    "Tim Butler",
    "Tina Lozano",
    "Tommy Wilcox",
    "Tommy-Lee Carter",
    "Tommy-Lee Hutchinson",
    "Tomos Martin",
    "Tony Hardin",
    "Vanessa Snow",
    "Veronica Hodges",
    "Victor Kelley",
    "Wiktor Holmes",
    "Wilfred Terry",
    "Willie Hoffman",
    "Wojciech Ray",
    "Yasir Lynn",
    "Yousef Glover",
    "Zane West",
    "Zarah Stephenson",
    "Zaynah Petty",
)
