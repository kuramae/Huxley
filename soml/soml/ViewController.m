//
//  ViewController.m
//  soml
//
//  Created by Anto on 30/12/13.
//  Copyright (c) 2013 Anto. All rights reserved.
//

#import "ViewController.h"
#import "EFCircularSlider.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
 

        CGRect sliderFrame = CGRectMake(110, 150, 100, 100);
        EFCircularSlider* circularSlider = [[EFCircularSlider alloc] initWithFrame:sliderFrame];
        [self.view addSubview:circularSlider];
        // test per vedere se va lo scroll rotondo
     
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
