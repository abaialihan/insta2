<template>
    <v-card>
        <v-card-actions>
            <v-row align="center" justify="end">
            <v-dialog v-model="dialog" width="500">
                <template v-slot:activator="{ on }">
                    <v-btn icon v-on="on">
                        <v-icon icon >mdi-dots-horizontal</v-icon>
                        <span class="mr-5"></span>
                    </v-btn>
                </template>
                <v-layout class="headline grey lighten-2" align-center justify-start column>
                    <v-btn icon @click="edit">Edit
                        <v-icon>mdi-pencil</v-icon>
                    </v-btn>
                    <v-btn icon @click="del">Delete
                        <v-icon>mdi-delete</v-icon>
                    </v-btn>
                </v-layout>
            </v-dialog>
            </v-row>
        </v-card-actions>
        <media v-if="message.link" :message="message"></media>
        <v-card-text class="my-2">
            <i>({{ message.id }})</i>
            {{ message.text }}
        </v-card-text>
        <v-card-actions>
            <v-btn>test</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import { mapActions } from 'vuex'
    import Media from 'components/media/Media.vue'

    export default {
        props: ['message', 'editMessage'],
        components: { Media },
        data () {
            return {
                dialog: false,
            }
        },
        methods: {
            ...mapActions(['removeMessageAction']),
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMessageAction(this.message)
            }
        }
    }
</script>

<style>

</style>