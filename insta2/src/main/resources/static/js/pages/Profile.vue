<template>
    <v-card
            class="mx-auto"
            max-width="1000"
            outlined>
        <v-card-actions justify-space-around align="center">
            <v-flex px-1 xs3>
                <v-avatar size="200">
                    <img :src="profile.avatar">
                </v-avatar>
            </v-flex>
            <v-flex xs2 >
                <v-layout mt-n12>
                    <v-row justify="end">
                        <div class="title">{{profile.name}}</div>
                        <span class="mr-10"></span>
                    </v-row>
                </v-layout>
            </v-flex>
            <v-flex xs2>
                <v-layout mt-n12>
                    <v-row justify="start">
                        <v-btn outlined>Edit profile</v-btn>
                    </v-row>
                </v-layout>
            </v-flex>
        </v-card-actions>
    </v-card>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'Profile',
        computed: mapState(['profile']),
    }
</script>

<style scoped>
    img{
        max-width: 100%;
        height: auto;
    }
</style>