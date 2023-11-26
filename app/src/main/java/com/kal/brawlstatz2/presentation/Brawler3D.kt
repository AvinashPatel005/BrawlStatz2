package com.kal.brawlstatz2.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.ar.core.Config
import io.github.sceneview.Scene
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import java.net.URI

@Composable
fun First(file: String) {

    var nodes = remember {
        mutableStateListOf<Node>()
    }
    var color =MaterialTheme.colorScheme.background
    var modelNode by remember {
        mutableStateOf<Node?>(null)
    }
        Scene(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            nodes = nodes,
            onCreate = {
                var disc = ModelNode(it.engine, modelGlbFileLocation = "disc.glb", scaleUnits = 0.3f).apply {
                    modelPosition= Position(0f,-0.203f,0f)
                }
                modelNode=ModelNode(it.engine, modelGlbFileLocation =file, scaleUnits = 0.7f).apply {
                    modelPosition= Position(0f,-0.2f,0f)
                }
                it.cameraNode.camera.setShift(0.0,0.1)
                nodes.add(disc)
                nodes.add(modelNode!!)
                it.skybox?.setColor(color.red,color.green,color.blue,0f)
            },
        )
}
@Composable
fun ModelScreen(file:String) {
    val nodes = remember { mutableStateListOf<ArNode>() }
    var modelNode by remember {
        mutableStateOf<ArModelNode?>(null)
    }
    var placeModel by remember {
        mutableStateOf(false)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneView ->
                arSceneView.lightEstimationMode= Config.LightEstimationMode.AMBIENT_INTENSITY
                arSceneView.planeRenderer.isShadowReceiver=true
                modelNode= ArModelNode(arSceneView.engine, placementMode = PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(file){
                        scaleModel(0.5f)
                        setCastShadows(true)
                    }
                    onAnchorChanged={
                        placeModel=!isAnchored
                        arSceneView.planeRenderer.isVisible=false
                        scaleModel(0.5f)
                    }
                    onHitResult ={node, hitResult ->
                        placeModel=node.isTracking
                    }
                }
                nodes.add(modelNode!!)



            },
        )
        if(placeModel){
            Button(modifier = Modifier.align(Alignment.BottomCenter),onClick = {

                modelNode?.anchor()
                modelNode?.scaleModel(0.5f)
            }) {
                Text(text = "Place")
            }
        }
    }
}
