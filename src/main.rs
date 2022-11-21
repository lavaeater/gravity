use bevy::prelude::*;
use bevy_prototype_lyon::prelude::*;


/*
SpriteBundle är en färdig-konstruerad bundle i bevy för att visa en sprite.
Det finns också en SpriteSheetBundle
Camera2dBundle är en kamera, yay.
 */


fn main() {
    App::new()
        // .insert_resource(Msaa { samples: 4 })
        .add_plugins(DefaultPlugins)
        // .add_plugin(ShapePlugin)
        .add_startup_system(setup_system)
        .run();
}

fn setup_system(mut commands: Commands) {
    // let shape = shapes::RegularPolygon {
    //     sides: 6,
    //     feature: shapes::RegularPolygonFeature::Radius(200.0),
    //     ..shapes::RegularPolygon::default()
    // };

    // commands.spawn(Camera2dBundle);
    // commands.spawn(GeometryBuilder::build_as(
    //     &shape,
    //     DrawMode::Outlined {
    //         fill_mode: FillMode::color(bevy_render::Color::CYAN),
    //         outline_mode: StrokeMode::new(bevy_render::Color::BLACK, 10.0),
    //     },
    //     bevy_transform::Transform::default(),
    // ));
}

// #[derive(Component)]
// struct Planet;
//
// #[derive(Component)]
// struct Mass(f64);

