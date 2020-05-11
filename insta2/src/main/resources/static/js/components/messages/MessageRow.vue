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
    export default {
        props: ['message', 'editMessage', 'deleteMessage', 'messages'],
        data () {
            return {
                dialog: false,
            }
        },
        methods: {
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.deleteMessage(this.message)
            },
        }
    }
</script>

<style>
</style>